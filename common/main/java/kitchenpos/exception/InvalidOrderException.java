package kitchenpos.exception;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(final String message) {
        super(message);
    }
}
