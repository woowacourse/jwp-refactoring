package kitchenpos.table.domain;

import org.springframework.stereotype.Component;

@Component
public class OrderTablesGroupingValidator implements OrderTablesValidator {

    private static final int MINIMUM_TABLE_SIZE = 2;

    @Override
    public void validate(final OrderTables orderTables) {
        validateSize(orderTables);
        validateGroupOrderTableIsAvailable(orderTables);
    }

    public void validateSize(final OrderTables orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException("Table group must have at least two tables.");
        }
    }

    public void validateGroupOrderTableIsAvailable(final OrderTables orderTables) {
        if (!isOrderTablesAbleToGroup(orderTables)) {
            throw new IllegalArgumentException("Cannot group non-empty table or already grouped table.");
        }
    }

    private boolean isOrderTablesAbleToGroup(final OrderTables orderTables) {
        return orderTables.getOrderTables().stream().allMatch(OrderTable::isAbleToGroup);
    }
}
