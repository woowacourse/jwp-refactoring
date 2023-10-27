package kitchenpos.tablegroup.application;

import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTables;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;

@Component
public class TableGroupValidator {
    private final OrderRepository orderRepository;

    public TableGroupValidator(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validate(
            final List<OrderTable> orderTables,
            final List<Long> longs) {
        validateOrderTableSize(orderTables, longs);
        validateOrderTablesStatus(orderTables);
    }

    private void validateOrderTableSize(final List<OrderTable> orderTables, final List<Long> requestIds) {
        if (orderTables.size() != requestIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesStatus(final List<OrderTable> orderTables) {
        for (OrderTable orderTable : orderTables) {
            validateOrderTableStatus(orderTable);
        }
    }

    private void validateOrderTableStatus(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.validateTableGroupIsNonNull()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderTableStatus(final OrderTables orderTables) {
        final List<Long> orderTableIds = orderTables.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
        if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(
                orderTableIds, List.of(COOKING, MEAL))) {
            throw new IllegalArgumentException();
        }
    }
}
