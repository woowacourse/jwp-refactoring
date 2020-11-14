package kitchenpos.fixture;

import kitchenpos.domain.OrderLineItem;
import kitchenpos.ui.dto.OrderLineItemCreateRequest;

public class OrderLineItemFixture {
    public static OrderLineItem createOrderLineItem(Long seq, Long orderId, Long menuId, int quantity) {
        OrderLineItem orderLineItem = createOrderLineItem(orderId, menuId, quantity);
        orderLineItem.setSeq(seq);
        return orderLineItem;
    }

    public static OrderLineItem createOrderLineItem(Long orderId, Long menuId, int quantity) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(quantity);
        orderLineItem.setOrderId(orderId);
        return orderLineItem;
    }

    public static OrderLineItemCreateRequest createOrderLineItemRequest(Long menuId, int quantity) {
        return new OrderLineItemCreateRequest(menuId, quantity);
    }
}
