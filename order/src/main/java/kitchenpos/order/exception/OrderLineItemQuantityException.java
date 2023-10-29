package kitchenpos.order.exception;

public class OrderLineItemQuantityException extends RuntimeException {

    public OrderLineItemQuantityException(String message) {
        super(message);
    }
}
