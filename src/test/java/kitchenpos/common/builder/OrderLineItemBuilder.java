package kitchenpos.common.builder;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemBuilder {

    private Long orderId;
    private Long menuId;
    private long quantity;

    public OrderLineItemBuilder orderId(final Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public OrderLineItemBuilder menuId(final Long menuId) {
        this.menuId = menuId;
        return this;
    }

    public OrderLineItemBuilder quantity(final long quantity) {
        this.quantity = quantity;
        return this;
    }

    public OrderLineItem build() {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
