package kitchenpos.domain;

import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class OrderTables {

    private List<OrderTable> value;

    private OrderTables() {
    }

    public OrderTables(final List<OrderTable> value) {
        validate(value);
        this.value = value;
    }

    private void validate(final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    public void group() {
        for (final OrderTable orderTable : value) {
            validateOrderTableIsAbleToGroup(orderTable);
            orderTable.setEmpty(false);
        }
    }

    private void validateOrderTableIsAbleToGroup(final OrderTable orderTable) {
        if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroupId())) {
            throw new IllegalArgumentException();
        }
    }

    public void ungroup() {
        for (final OrderTable orderTable : value) {
            orderTable.ungroup();
        }
    }

    public List<OrderTable> getOrderTables() {
        return value;
    }
}
