package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemResponse {

    private final Long id;
    private final Long orderId;
    private final Long menuId;
    private final long quantity;

    public OrderLineItemResponse(Long id, Long orderId, Long menuId, long quantity) {
        this.id = id;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse toResponse(OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(orderLineItem.getId(), orderLineItem.getOrderId(), orderLineItem.getMenuId(),
            orderLineItem.getQuantity());
    }
}
