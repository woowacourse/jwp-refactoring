package kitchenpos.table.domain;

import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public OrderTables() {
        this(new ArrayList<>());
    }

    public boolean isDifferentSize(final int size) {
        return orderTables.size() != size;
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(orderTables);
    }

    public int size() {
        return orderTables.size();
    }

    public List<Long> extractOrderTableIds() {
        return orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());
    }

    public void validateAllOrderTablesEmptyAndNotHaveTableGroup() {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || orderTable.hasTableGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<OrderTable> ungroup() {
        return orderTables.stream()
                .map(orderTable -> new OrderTable(orderTable.getId(), null, orderTable.getNumberOfGuests(),
                        false))
                .collect(Collectors.toList());
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
