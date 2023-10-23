package kitchenpos.domain.ordertable;

import java.util.List;
import java.util.Objects;

public class OrderTables {

    private List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        validateOrderTableSize(orderTables);
        validateOrderTableIsNotEmptyOfAlreadyContainedOtherTableGroup(orderTables);
        this.orderTables = orderTables;
    }

    public void validateOrderTableSize(final List<OrderTable> orderTables) {
        if (orderTables.isEmpty() || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void validateOrderTableIsNotEmptyOfAlreadyContainedOtherTableGroup(final List<OrderTable> orderTables) {
        for (final OrderTable orderTable : orderTables) {
            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
