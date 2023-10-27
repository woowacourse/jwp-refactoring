package com.kitchenpos.application.dto;

public class OrderTableIdRequest {

    private final Long id;

    public OrderTableIdRequest(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
