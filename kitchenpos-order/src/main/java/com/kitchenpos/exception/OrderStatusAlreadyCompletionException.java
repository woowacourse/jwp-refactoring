package com.kitchenpos.exception;

public class OrderStatusAlreadyCompletionException extends RuntimeException{

    public OrderStatusAlreadyCompletionException() {
        super("이미 완료된 주문 상태를 변경할 수 없습니다.");
    }
}
