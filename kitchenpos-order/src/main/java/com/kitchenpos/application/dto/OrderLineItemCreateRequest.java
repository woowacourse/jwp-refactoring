package com.kitchenpos.application.dto;

import com.kitchenpos.domain.OrderLineItem;

public class OrderLineItemCreateRequest {

    private Long menuId;
    private Long quantity;

    private OrderLineItemCreateRequest() {
    }

    public OrderLineItemCreateRequest(final Long menuId, final long quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public OrderLineItem toDomain() {
        return new OrderLineItem(menuId, quantity);
    }

    public Long getMenuId() {
        return menuId;
    }

    public long getQuantity() {
        return quantity;
    }
}
