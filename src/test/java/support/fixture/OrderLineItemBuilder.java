package support.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemBuilder {

    private Order order;
    private Menu menu;
    private long quantity;

    public OrderLineItemBuilder() {
        order = null;
        menu = null;
        quantity = 0;
    }

    public OrderLineItemBuilder setOrder(final Order order) {
        this.order = order;
        return this;
    }

    public OrderLineItemBuilder setMenu(final Menu menu) {
        this.menu = menu;
        return this;
    }

    public OrderLineItemBuilder setQuantity(final long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(order, menu, quantity);
    }
}
