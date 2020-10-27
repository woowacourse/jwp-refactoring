package kitchenpos.domain;

import kitchenpos.domain.exceptions.InvalidOrderTableSizesException;
import org.springframework.util.CollectionUtils;

import java.util.List;

public class OrderTables {

    private final List<OrderTable> orderTables;

    private OrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public static OrderTables of(final List<Long> orderTableIds, final List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTableIds) || orderTableIds.size() < 2) {
            throw new InvalidOrderTableSizesException();
        }

        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }

        return new OrderTables(orderTables);
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
