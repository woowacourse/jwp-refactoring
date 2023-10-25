package kitchenpos.table.application;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupRequest.OrderTableDto;
import kitchenpos.table.dto.TableGroupResponse;
import kitchenpos.table.exception.RequestOrderTableCountNotEnoughException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.TableGroupNotFoundException;
import kitchenpos.table.exception.UnCompletedOrderExistsException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
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

    @Transactional
    public TableGroupResponse create(TableGroupRequest request) {
        validateOrderTableCountFromRequest(request);
        List<OrderTable> orderTables = validateOrderTableExistence(request);
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup(LocalDateTime.now()));
        for (OrderTable orderTable : orderTables) {
            orderTable.addToTableGroup(tableGroup);
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

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        validateEveryOrderInGroupCompleted(tableGroup);
        tableGroupRepository.delete(tableGroup);
    }

    private void validateEveryOrderInGroupCompleted(TableGroup tableGroup) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroup.getId());
        List<Orders> orders = orderRepository.findAllByOrderTableIn(orderTables);

        boolean isUncompletedOrderExists = orders.stream()
                .anyMatch(Orders::isOrderUnCompleted);
        if (isUncompletedOrderExists) {
            throw new UnCompletedOrderExistsException();
        }
    }
}
