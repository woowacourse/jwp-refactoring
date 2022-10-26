package kitchenpos.domain.table;

import java.util.List;
import java.util.Objects;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private static void validate(List<OrderTable> orderTables) {
        boolean isOrderTableOrGrouped = orderTables.stream()
                .anyMatch(it -> !it.isEmpty() || Objects.nonNull(it.getTableGroupId()));
        if (isOrderTableOrGrouped) {
            throw new IllegalArgumentException();
        }
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
