package kitchenpos.exception;

public class InvalidMenuException extends RuntimeException {
    public InvalidMenuException(final String message) {
        super(message);
    }
}
