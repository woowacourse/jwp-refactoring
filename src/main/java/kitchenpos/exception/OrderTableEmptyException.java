package kitchenpos.exception;

public class OrderTableEmptyException extends RuntimeException {
    public OrderTableEmptyException() {
        super("주문테이블이 비어있으면 안됩니다.");
    }
}
