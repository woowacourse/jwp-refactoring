package support.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order_line_item.OrderLineItem;

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
