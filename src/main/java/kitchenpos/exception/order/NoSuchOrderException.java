package kitchenpos.exception.order;

public class NoSuchOrderException extends RuntimeException {
    private static final String MESSAGE = "주문을 찾을 수 없습니다.";

    public NoSuchOrderException() {
        super(MESSAGE);
    }
}
