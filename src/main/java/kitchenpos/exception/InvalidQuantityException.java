package kitchenpos.exception;

public class InvalidQuantityException extends IllegalArgumentException {

    private static final String MESSAGE = "수량은 0보다 커야합니다.";

    public InvalidQuantityException() {
        super(MESSAGE);
    }
}
