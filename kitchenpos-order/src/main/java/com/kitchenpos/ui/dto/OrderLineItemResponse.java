package com.kitchenpos.ui.dto;

import com.kitchenpos.domain.OrderLineItem;

public class OrderLineItemResponse {

    private Long seq;
    private Long orderId;
    private Long menuId;
    private Long quantity;

    private OrderLineItemResponse() {
    }

    private OrderLineItemResponse(final Long seq,
                                  final Long orderId,
                                  final Long menuId,
                                  final Long quantity) {
        this.seq = seq;
        this.orderId = orderId;
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public static OrderLineItemResponse of(final OrderLineItem orderLineItem, final Long orderId) {
        return new OrderLineItemResponse(
                orderLineItem.getSeq(),
                orderId,
                orderLineItem.getMenuId(),
                orderLineItem.getQuantity()
        );
    }

    public Long getSeq() {
        return seq;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
