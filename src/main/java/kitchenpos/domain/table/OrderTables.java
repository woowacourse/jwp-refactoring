package kitchenpos.domain.table;

import java.util.List;

public class OrderTables {

    private static final int MINIMUM_TABLE_COUNT = 2;

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        if (orderTables.size() < MINIMUM_TABLE_COUNT) {
            throw new IllegalArgumentException();
        }
        this.orderTables = orderTables;
    }

    public void group(Long tableGroupId) {
        validateGroupableTables();
        for (final OrderTable orderTable : orderTables) {
            orderTable.addTableGroupId(tableGroupId);
        }
    }

    private void validateGroupableTables() {
        boolean includesGroupedOrOrderTable = orderTables.stream()
                .anyMatch(OrderTable::isGroupedOrNotEmpty);
        if (includesGroupedOrOrderTable) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getValue() {
        return orderTables;
    }
}
