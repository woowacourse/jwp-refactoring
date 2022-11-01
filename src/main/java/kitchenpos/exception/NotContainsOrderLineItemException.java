package kitchenpos.exception;

public class NotContainsOrderLineItemException extends RuntimeException {
    public NotContainsOrderLineItemException() {
        super("주문 제품이 존재하지 않습니다.");
    }
}
