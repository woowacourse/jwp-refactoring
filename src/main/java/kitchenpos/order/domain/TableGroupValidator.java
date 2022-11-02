package kitchenpos.order.domain;

import java.util.List;

import org.springframework.util.CollectionUtils;

public class TableGroupValidator {

    public void validateOrderTables(final List<OrderTable> orderTables) {
        validateOrderTablesSize(orderTables);
        validateOrderTablesCanGroup(orderTables);
    }

    private void validateOrderTablesSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTablesCanGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            orderTable.validateCanBeGrouped();
        }
    }
}
