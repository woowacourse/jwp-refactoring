package kitchenpos.order.exception;

public class InvalidOrderLineItemCreateException extends RuntimeException {

    public InvalidOrderLineItemCreateException() {
        super("주문 항목을 생성할 수 없습니다.");
    }
}
