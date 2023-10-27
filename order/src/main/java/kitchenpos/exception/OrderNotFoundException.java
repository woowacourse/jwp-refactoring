package kitchenpos.exception;

public class OrderNotFoundException extends RuntimeException {

    private static final String MESSAGE = "주문을 찾을 수 없습니다.";

    public OrderNotFoundException() {
        super(MESSAGE);
    }
}
