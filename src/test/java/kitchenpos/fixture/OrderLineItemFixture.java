package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem createOrderLineItem(Long seq, Long menuId, Long orderId, Long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setSeq(seq);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setOrderId(orderId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }

    public static OrderLineItem createOrderLineItemWithMenuId(Long menuId) {
        return createOrderLineItem(null, menuId, null, 1L);
    }

}
