package kitchenpos.ui.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private long quantity;

    protected OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
