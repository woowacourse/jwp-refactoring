package kitchenpos.dto.request;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private int quantity;

    private OrderLineItemRequest() {
    }

    public OrderLineItemRequest(final Long menuId, final int quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public int getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity(final Long orderId) {
        return new OrderLineItem(orderId, menuId, quantity);
    }
}
