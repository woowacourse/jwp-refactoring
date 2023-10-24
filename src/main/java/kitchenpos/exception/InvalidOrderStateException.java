package kitchenpos.exception;

public class InvalidOrderStateException extends RuntimeException {

    public InvalidOrderStateException(final String message) {
        super(message);
    }
}
