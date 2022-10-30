package kitchenpos.order.application.dto;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemSaveRequest {

    private final Long menuId;
    private final Long quantity;

    public OrderLineItemSaveRequest(Long menuId, Long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity(Long orderId) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
