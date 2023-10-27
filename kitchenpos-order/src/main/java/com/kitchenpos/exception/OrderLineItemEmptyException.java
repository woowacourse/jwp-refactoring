package com.kitchenpos.exception;

public class OrderLineItemEmptyException extends RuntimeException {

    public OrderLineItemEmptyException() {
        super("주문 항목이 비어있습니다.");
    }
}
