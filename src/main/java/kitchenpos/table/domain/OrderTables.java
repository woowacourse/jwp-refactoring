package kitchenpos.table.domain;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderTables {
    private static final int MIN_TABLES_COUNT = 2;

    private final List<OrderTable> orderTables;
    public OrderTables(List<OrderTable> orderTables) {
        validateOrderTables(orderTables);
        this.orderTables = orderTables;
    }

    private void validateOrderTables(List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesAlreadyInTableGroup(orderTables);
    }

    private void validateOrderTablesSize(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MIN_TABLES_COUNT) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesAlreadyInTableGroup(List<OrderTable> orderTables) {
        if (hasInvalidOrderTable(orderTables)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean hasInvalidOrderTable(List<OrderTable> orderTables) {
        return orderTables
                .stream()
                .anyMatch(orderTable -> !orderTable.isEmpty() || orderTable.hasTableGroupId());
    }

    public List<OrderTable> getOrderTables() {
        return this.orderTables;
    }
}
