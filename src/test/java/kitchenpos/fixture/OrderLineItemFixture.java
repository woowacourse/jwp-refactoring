package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem createWithoutId(Long menuId, Long orderId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setOrderId(orderId);

        return orderLineItem;
    }
}
