package kitchenpos.exception.quantity;

public class InvalideQuantityValueException extends RuntimeException {
    private static final String MESSAGE = "수량은 0보다 작거나 Null일 수 없습니다.";

    public InvalideQuantityValueException() {
        super(MESSAGE);
    }
}
