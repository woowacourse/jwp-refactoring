package support.fixture;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order_line_item.OrderLineItem;

public class OrderLineItemBuilder {

    private final OrderLineItem orderLineItem;

    public OrderLineItemBuilder(final Menu menu, final Integer quantity) {
        this.orderLineItem = new OrderLineItem();
        orderLineItem.setMenu(menu);
        orderLineItem.setQuantity(quantity);
    }

    public OrderLineItemBuilder setOrderId(final Order order) {
        orderLineItem.setOrder(order);
        return this;
    }

    public OrderLineItem build() {
        return orderLineItem;
    }
}
