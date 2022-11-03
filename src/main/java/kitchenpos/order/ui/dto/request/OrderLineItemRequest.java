package kitchenpos.order.ui.dto.request;

import kitchenpos.order.domain.OrderLineItem;

public class OrderLineItemRequest {

    private final Long menuId;
    private final long quantity;

    public OrderLineItemRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }
}
