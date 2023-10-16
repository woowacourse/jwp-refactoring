package support.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemBuilder {

    private final OrderLineItem orderLineItem;

    public OrderLineItemBuilder(final Menu menu, final Integer quantity) {
        this.orderLineItem = new OrderLineItem();
        orderLineItem.setMenu(menu);
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
