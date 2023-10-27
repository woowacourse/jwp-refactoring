package kitchenpos.order.exception;

public class NotExistOrderException extends RuntimeException {
    public NotExistOrderException(final String message) {
        super(message);
    }
}
