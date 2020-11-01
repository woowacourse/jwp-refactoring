package kitchenpos.application;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    static OrderLineItem createOrderLineItem(Long orderId, Long menuId, Long quantity) {
        OrderLineItem orderLineItem = createOrderLineItem(menuId, quantity);
        orderLineItem.setOrderId(orderId);
        return orderLineItem;
    }

    static OrderLineItem createOrderLineItem(Long menuId, Long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
