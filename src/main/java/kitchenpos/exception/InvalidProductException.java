package kitchenpos.exception;

public class InvalidProductException extends IllegalArgumentException {

    public InvalidProductException() {
        super();
    }

    public InvalidProductException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
