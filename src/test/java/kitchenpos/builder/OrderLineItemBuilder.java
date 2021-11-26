package kitchenpos.builder;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemBuilder {

    private Long seq;
    private Order order;
    private Menu menu;
    private long quantity;

    public OrderLineItemBuilder seq(Long seq) {
        this.seq = seq;
        return this;
    }

    public OrderLineItemBuilder order(Order order) {
        this.order = order;
        return this;
    }

    public OrderLineItemBuilder menu(Menu menu) {
        this.menu = menu;
        return this;
    }

    public OrderLineItemBuilder quantity(long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(
                this.seq,
                this.order,
                this.menu,
                this.quantity
        );
    }
}
