package kitchenpos.exception;

public class InvalidOrderLineItemsException extends IllegalArgumentException {

    public InvalidOrderLineItemsException(final String message) {
        super(message);
    }
}
