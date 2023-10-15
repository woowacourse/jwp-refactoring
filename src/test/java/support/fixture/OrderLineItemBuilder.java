package support.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemBuilder {

    private final OrderLineItem orderLineItem;

    public OrderLineItemBuilder(final Long menuId, final Integer quantity) {
        this.orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
    }

    public OrderLineItemBuilder setOrderId(final Long orderId) {
        orderLineItem.setOrderId(orderId);
        return this;
    }

    public OrderLineItem build() {
        return orderLineItem;
    }
}
