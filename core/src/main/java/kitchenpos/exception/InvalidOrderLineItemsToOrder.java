package kitchenpos.exception;

public class InvalidOrderLineItemsToOrder extends IllegalArgumentException {

    public InvalidOrderLineItemsToOrder(final String message) {
        super(message);
    }
}
