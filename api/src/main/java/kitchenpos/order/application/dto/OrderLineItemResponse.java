package kitchenpos.order.application.dto;

import kitchenpos.order.OrderLineItem;

public class OrderLineItemResponse {

    private long menuId;
    private long quantity;

    public OrderLineItemResponse(final long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(final OrderLineItem orderLineItem) {
        return new OrderLineItemResponse(
                orderLineItem.getMenuId(), orderLineItem.getQuantity()
        );
    }

    public long getQuantity() {
        return quantity;
    }
}
