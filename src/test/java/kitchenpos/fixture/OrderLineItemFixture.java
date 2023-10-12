package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemFixture {

    public static OrderLineItem 주문_항목(Long orderId, Long menuId, long quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(orderId);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        return orderLineItem;
    }
}
