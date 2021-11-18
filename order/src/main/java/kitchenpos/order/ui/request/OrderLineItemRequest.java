package kitchenpos.order.ui.request;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private long quantity;

    public static OrderLineItemRequest create(Long id, int quantity) {
        final OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest();
        orderLineItemRequest.menuId = id;
        orderLineItemRequest.quantity = quantity;
        return orderLineItemRequest;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity() {
        return OrderLineItem.create(menuId, quantity);
    }
}
