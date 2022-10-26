package kitchenpos.domain.table;

import java.util.List;
import java.util.Objects;

public class EmptyTables {

    private static final int MINIMUM_TABLE_COUNT = 2;

    private final List<OrderTable> orderTables;

    public EmptyTables(List<OrderTable> orderTables) {
        validate(orderTables);
        this.orderTables = orderTables;
    }

    private static void validate(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_TABLE_COUNT) {
            throw new IllegalArgumentException();
        }
        boolean includesGroupedOrOrderTable = orderTables.stream()
                .anyMatch(it -> !it.isEmpty() || Objects.nonNull(it.getTableGroupId()));
        if (includesGroupedOrOrderTable) {
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
