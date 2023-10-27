package kitchenpos.order.exception;

public class OrderLineItemException extends RuntimeException {
    public OrderLineItemException(final String message) {
        super(message);
    }
}
