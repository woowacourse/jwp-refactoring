package kitchenpos.exception;

public class InvalidOrderStatusException extends RuntimeException {
    public InvalidOrderStatusException() {
        super("주문의 상태가 적절하지 않습니다.");
    }
}
