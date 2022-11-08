package kitchenpos.common.builder;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderMenu;

public class OrderLineItemBuilder {

    private Long orderId;
    private OrderMenu orderMenu;
    private long quantity;

    public OrderLineItemBuilder orderId(final Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderLineItemBuilder orderMenu(final OrderMenu orderMenu) {
        this.orderMenu = orderMenu;
        return this;
    }

    public OrderLineItemBuilder quantity(final long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(orderMenu, quantity);
    }
}
