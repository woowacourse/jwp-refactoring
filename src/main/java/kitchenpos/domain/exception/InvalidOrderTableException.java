package kitchenpos.domain.exception;

public class InvalidOrderTableException extends RuntimeException {
    public InvalidOrderTableException(final String message) {
        super(message);
    }
}
