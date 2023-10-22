package kitchenpos.domain.table;

import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class OrderTablesValidator {

    private static final int MINIMUM_TABLE_SIZE = 2;

    public void validateSize(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < MINIMUM_TABLE_SIZE) {
            throw new IllegalArgumentException("Table group must have at least two tables.");
        }
    }

    public void validateGroupOrderTableIsAvailable(final List<OrderTable> orderTables) {
        if (!isOrderTablesAbleToGroup(orderTables)) {
            throw new IllegalArgumentException("Cannot group non-empty table or already grouped table.");
        }
    }

    private boolean isOrderTablesAbleToGroup(final List<OrderTable> orderTables) {
        return orderTables.stream().allMatch(OrderTable::isAbleToGroup);
    }
}
