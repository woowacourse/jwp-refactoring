package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables ofNotGroupedOrderTables(List<OrderTable> orderTables) {
        boolean isOrderTableOrGrouped = orderTables.stream()
                .anyMatch(it -> !it.isEmpty() || Objects.nonNull(it.getTableGroupId()));
        if (isOrderTableOrGrouped) {
            throw new IllegalArgumentException();
        }
        return new OrderTables(orderTables);
    }

    public void group(Long tableGroupId) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.addTableGroupId(tableGroupId);
        }
    }

    public List<OrderTable> getValue() {
        return orderTables;
    }
}
