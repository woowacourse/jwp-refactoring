package kitchenpos.tablegroup.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest.OrderTableDto;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import kitchenpos.tablegroup.exception.OrderTableNotFoundException;
import kitchenpos.tablegroup.exception.RequestOrderTableCountNotEnoughException;
import kitchenpos.tablegroup.exception.TableGroupNotFoundException;
import kitchenpos.tablegroup.exception.UnCompletedOrderExistsException;
import kitchenpos.tablegroup.repository.OrderRepository;
import kitchenpos.tablegroup.repository.OrderTableRepository;
import kitchenpos.tablegroup.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@Transactional
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository,
            OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    public TableGroupResponse create(TableGroupRequest request) {
        validateOrderTableCountFromRequest(request);
        List<OrderTable> orderTables = validateOrderTableExistence(request);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        for (OrderTable orderTable : orderTables) {
            orderTable.addToTableGroup(tableGroup.getId());
        }
        return TableGroupResponse.of(tableGroup, orderTables);
    }

    private void validateOrderTableCountFromRequest(TableGroupRequest request) {
        List<OrderTableDto> orderTableDtos = request.getOrderTableDtos();
        if (CollectionUtils.isEmpty(orderTableDtos) || orderTableDtos.size() < 2) {
            throw new RequestOrderTableCountNotEnoughException();
        }
    }

    private List<OrderTable> validateOrderTableExistence(TableGroupRequest request) {
        List<Long> orderTableIds = request.getOrderTableDtos().stream()
                .map(OrderTableDto::getId)
                .collect(toList());
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        if (orderTables.size() != orderTableIds.size()) {
            throw new OrderTableNotFoundException();
        }
        return orderTables;
    }

    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        validateEveryOrderInGroupCompleted(tableGroup);
        tableGroupRepository.delete(tableGroup);
    }

    private void validateEveryOrderInGroupCompleted(TableGroup tableGroup) {
        List<Long> orderTableIds = orderTableRepository.findAllByTableGroupId(tableGroup.getId())
                .stream()
                .map(OrderTable::getId)
                .collect(toList());
        List<Order> orders = orderRepository.findAllByOrderTableIdIn(orderTableIds);

        boolean isUncompletedOrderExists = orders.stream()
                .anyMatch(Order::isOrderUnCompleted);
        if (isUncompletedOrderExists) {
            throw new UnCompletedOrderExistsException();
        }
    }
}
