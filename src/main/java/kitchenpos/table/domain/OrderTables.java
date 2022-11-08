package kitchenpos.table.domain;

import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void validateOrderTables() {
        for (final OrderTable orderTable : orderTables) {
            validateOrderTable(orderTable);
        }
    }

    private void validateOrderTable(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || orderTable.hasTableGroupId()) {
            throw new IllegalArgumentException();
        }
    }

    public int size() {
        return orderTables.size();
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTable> ungroup() {
        return orderTables.stream()
                .map(OrderTable::ungroup)
                .collect(Collectors.toList());
    }
}
