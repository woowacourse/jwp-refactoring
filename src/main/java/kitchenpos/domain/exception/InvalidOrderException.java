package kitchenpos.domain.exception;

public class InvalidOrderException extends RuntimeException {
    public InvalidOrderException(final String message) {
        super(message);
    }
}
