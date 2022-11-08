package kitchenpos.exception;

public class OrderLineItemRemoveFailException extends RuntimeException {
    public OrderLineItemRemoveFailException() {
        super("주문 목록을 제거하지 못했습니다.");
    }
}
