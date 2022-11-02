package kitchenpos.domain;

import java.util.List;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private final List<OrderTable> orderTables;

    public OrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void validatePossibleBindTableGroup() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void validateMakeTableGroup(int size) {
        if (orderTables.size() != size) {
            throw new IllegalArgumentException();
        }

        for (final OrderTable savedOrderTable : getOrderTables()) {
            if (!savedOrderTable.isEmpty() || savedOrderTable.isNonNullTableGroup()) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public int getOrderTablesSize() {
        return orderTables.size();
    }
}
