package kitchenpos.exception;

public class InvalidOrderLineItemQuantityException extends RuntimeException {
    public InvalidOrderLineItemQuantityException(String message) {
        super(message);
    }
}
