package kitchenpos.domain.exception;

public class InvalidOrderChangeException extends RuntimeException {
    public InvalidOrderChangeException(final String message) {
        super(message);
    }
}
