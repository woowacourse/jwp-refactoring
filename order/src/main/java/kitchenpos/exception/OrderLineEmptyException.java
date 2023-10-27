package kitchenpos.exception;

public class OrderLineEmptyException extends RuntimeException {

    private static final String MESSAGE = "주문 항목은 비어 있을 수 없습니다.";

    public OrderLineEmptyException() {
        super(MESSAGE);
    }
}
