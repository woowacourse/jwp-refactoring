package kitchenpos.exception;

public class InvalidOrderTableException extends IllegalArgumentException {

    public InvalidOrderTableException(final String message) {
        super(message);
    }
}
