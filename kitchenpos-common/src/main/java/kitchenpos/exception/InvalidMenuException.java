package kitchenpos.exception;

public class InvalidMenuException extends IllegalArgumentException {

    public InvalidMenuException(final String message) {
        super(message);
    }
}
