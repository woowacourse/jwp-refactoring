package kitchenpos.application.dto;

import kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateRequest {
    private Long menuId;
    private long quantity;

    private OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(Long menuId, long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toEntity() {
        return new OrderLineItem(null, null, menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
