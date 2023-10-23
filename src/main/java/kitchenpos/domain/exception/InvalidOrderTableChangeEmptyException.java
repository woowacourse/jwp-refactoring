package kitchenpos.domain.exception;

public class InvalidOrderTableChangeEmptyException extends RuntimeException {
    public InvalidOrderTableChangeEmptyException(final String message) {
        super(message);
    }
}
