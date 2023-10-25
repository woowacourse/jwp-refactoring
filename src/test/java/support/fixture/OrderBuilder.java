package support.fixture;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;

public class OrderBuilder {

    private OrderTable orderTable;

    public OrderBuilder() {
        this.orderTable = null;
    }

    public OrderBuilder setOrderTable(final OrderTable orderTable) {
        this.orderTable = orderTable;
        return this;
    }

    public Order build() {
        return new Order(orderTable);
    }
}
