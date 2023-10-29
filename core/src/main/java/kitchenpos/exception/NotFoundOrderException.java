package kitchenpos.exception;

public class NotFoundOrderException extends IllegalArgumentException {

    public NotFoundOrderException(final String message) {
        super(message);
    }
}
