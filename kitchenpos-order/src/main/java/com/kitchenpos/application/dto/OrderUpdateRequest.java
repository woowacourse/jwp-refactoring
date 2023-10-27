package com.kitchenpos.application.dto;

public class OrderUpdateRequest {

    private String orderStatus;

    private OrderUpdateRequest() {
    }

    public OrderUpdateRequest(final String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
