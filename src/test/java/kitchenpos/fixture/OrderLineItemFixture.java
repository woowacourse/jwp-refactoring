package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.dto.request.OrderLineItemRequest;

public class OrderLineItemFixture {


    public static OrderLineItemRequest generateOrderLineItemRequest(Long menuId, Long quantity) {
        return new OrderLineItemRequest(menuId, quantity);
    }

    public static OrderLineItem generateOrderLineItem(Long menuId, Long quantity, Long orderId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setQuantity(quantity);
        orderLineItem.setMenuId(menuId);
        orderLineItem.setOrderId(orderId);
        return orderLineItem;
    }
}
