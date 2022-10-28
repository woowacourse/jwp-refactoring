package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private List<OrderTable> orderTables;

    public OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public void group() {
        validateGroupSize();
        for (final OrderTable orderTable : orderTables) {
            validateOrderTableIsAbleToGroup(orderTable);
            orderTable.setEmpty(false);
        }
    }

    public void ungroup() {
        for (final OrderTable orderTable : orderTables) {
            orderTable.ungroup();
        }
    }

    public void validateGroupSize() {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTableIsAbleToGroup(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public int size() {
        return orderTables.size();
    }
}
