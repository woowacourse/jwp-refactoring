package kitchenpos.common.builder;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemBuilder {

    private Long orderId;
    private Menu menu;
    private long quantity;

    public OrderLineItemBuilder orderId(final Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderLineItemBuilder menu(final Menu menu) {
        this.menu = menu;
        return this;
    }

    public OrderLineItemBuilder quantity(final long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(menu, quantity);
    }
}
