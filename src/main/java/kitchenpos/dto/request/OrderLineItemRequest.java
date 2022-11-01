package kitchenpos.dto.request;

import kitchenpos.domain.order.OrderLineItem;

public class OrderLineItemRequest {

    private Long menuId;
    private long quantity;

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return OrderLineItem.ofNew(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    private OrderLineItemRequest() {
    }
}
