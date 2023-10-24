package kitchenpos.application;

import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.domain.table.TableGroupRepository;
import kitchenpos.dto.table.CreateTableGroupRequest;
import kitchenpos.dto.table.OrderTableRequest;
import kitchenpos.dto.table.TableGroupResponse;
import kitchenpos.dto.table.UnGroupRequest;
import kitchenpos.exception.order.OrderNotFoundException;
import kitchenpos.exception.order.OrderTableNotFoundException;
import kitchenpos.exception.table.OrderIsNotCompletedBadRequestException;
import kitchenpos.exception.table.TableGroupNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableGroupService {
    private final OrderRepository orderRepository;
    private final OrderTableRepository orderTableRepository;
    private final TableGroupRepository tableGroupRepository;

    public TableGroupService(final OrderRepository orderRepository, final OrderTableRepository orderTableRepository, final TableGroupRepository tableGroupRepository) {
        this.orderRepository = orderRepository;
        this.orderTableRepository = orderTableRepository;
        this.tableGroupRepository = tableGroupRepository;
    }

    @Transactional
    public TableGroupResponse create(final CreateTableGroupRequest request) {
        final List<OrderTable> orderTables = convertToOrderTables(request);
        final TableGroup tableGroup = TableGroup.of(orderTables);
        orderTables.forEach(orderTable -> orderTable.setTableGroup(tableGroup));
        return TableGroupResponse.from(tableGroupRepository.save(tableGroup));
    }

    private List<OrderTable> convertToOrderTables(final CreateTableGroupRequest request) {
        return request.getOrderTables().stream()
                .map(this::checkOrderTableExists)
                .collect(Collectors.toList());
    }

    private OrderTable checkOrderTableExists(final OrderTableRequest orderTableRequest) {
        return orderTableRepository.findById(orderTableRequest.getId())
                .orElseThrow(() -> new OrderTableNotFoundException(orderTableRequest.getId()));
    }

    @Transactional
    public void ungroup(final UnGroupRequest request) {
        final TableGroup tableGroup = convertToTableGroup(request.getTableGroupId());
        final List<OrderTable> orderTables = convertToOrderTables(request.getTableGroupId());
        final List<Order> orders = convertToOrders(orderTables);
        checkOrderCompleted(orders);
        tableGroup.ungroup();
    }

    private static void checkOrderCompleted(final List<Order> orders) {
        orders.forEach(order -> {
            if (order.isNotCompleted()) {
                throw new OrderIsNotCompletedBadRequestException(order.getId());
            }
        });
    }

    private TableGroup convertToTableGroup(final Long tableGroupId) {
        return tableGroupRepository.findById(tableGroupId)
                .orElseThrow(() -> new TableGroupNotFoundException(tableGroupId));
    }

    private List<Order> convertToOrders(final List<OrderTable> orderTables) {
        return orderTables.stream()
                .map(orderTable -> orderRepository.findByOrderTableId(orderTable.getId()))
                .map(maybeOrderTable -> maybeOrderTable.orElseThrow(OrderNotFoundException::new))
                .collect(Collectors.toList());
    }

    private List<OrderTable> convertToOrderTables(final Long tableGroupId) {
        return orderTableRepository.findAllByTableGroupId(tableGroupId);
    }
}
