package kitchenpos.order.exception;

public class NotExistOrderStatus extends RuntimeException {
    public NotExistOrderStatus(final String message) {
        super(message);
    }
}
