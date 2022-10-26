package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem create(final Menu menu) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1L);
        return orderLineItem;
    }
    public static OrderLineItem create(final Menu menu, final Order order) {
        final OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menu.getId());
        orderLineItem.setQuantity(1L);
        orderLineItem.setOrderId(order.getId());
        return orderLineItem;
    }
}
