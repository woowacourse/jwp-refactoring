package kitchenpos.exception;

public class NotFoundOrderTableException extends IllegalArgumentException {

    public NotFoundOrderTableException(final String message) {
        super(message);
    }
}
