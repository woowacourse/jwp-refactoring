package kitchenpos.order.exception;

public class NotExistOrderStatusException extends RuntimeException {
    public NotExistOrderStatusException(final String message) {
        super(message);
    }
}
