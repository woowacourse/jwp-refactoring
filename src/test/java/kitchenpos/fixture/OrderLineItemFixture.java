package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {
    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, Long quantity) {
        OrderLineItem orderLineItem = createOrderLineItem(orderId, menuId, quantity);
        orderLineItem.setSeq(seq);
        return orderLineItem;
    }

    public static OrderLineItem createOrderLineItem(Long orderId, Long menuId, Long quantity) {
        OrderLineItem orderLineItem = createOrderLineItem(menuId, quantity);
        orderLineItem.setOrderId(orderId);
        return orderLineItem;
    }

    public static OrderLineItem createOrderLineItem(Long menuId, Long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
