package kitchenpos.table.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.CreateTableGroupRequest;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import kitchenpos.table.exception.OrderTableCountNotEnoughException;
import kitchenpos.table.exception.OrderTableNotFoundException;
import kitchenpos.table.exception.TableGroupNotFoundException;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.repository.TableGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TableGroupService {

    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(OrderRepository orderRepository, OrderTableRepository orderTableRepository,
            TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(CreateTableGroupRequest request) {
        List<OrderTable> orderTables = findOrderTables(request.getOrderTables());
        TableGroup tableGroup = new TableGroup(LocalDateTime.now());
        groupOrderTables(orderTables);
        tableGroupRepository.save(tableGroup);

        return TableGroupResponse.from(tableGroup, orderTables);
    }

    private List<OrderTable> findOrderTables(List<OrderTableRequest> orderTableRequests) {
        return orderTableRequests.stream()
                .map(each -> orderTableRepository.findById(each.getId())
                        .orElseThrow(OrderTableNotFoundException::new))
                .collect(Collectors.toList());
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        TableGroup tableGroup = tableGroupRepository.findById(tableGroupId)
                .orElseThrow(TableGroupNotFoundException::new);
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroup(tableGroup);
        validateAllOrderCompleted(orderTables);
        unGroupOrderTables(orderTables);
    }

    private void validateAllOrderCompleted(List<OrderTable> orderTables) {
        List<Long> orderTableIds = orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        List<Order> orders = orderRepository.findByOrderTableIdIn(orderTableIds);

        for (Order order : orders) {
            order.validateOrderIsCompleted();
        }
    }

    public void groupOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.validateIsEmpty();
            orderTable.validateTableGroupNotExists();
        }
        validateOrderTableCount(orderTables);
    }

    private void validateOrderTableCount(List<OrderTable> orderTables) {
        if (orderTables.size() < 2) {
            throw new OrderTableCountNotEnoughException();
        }
    }

    public void unGroupOrderTables(List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            orderTable.changeTableGroup(null);
        }
    }

}
