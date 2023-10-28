package kitchenpos.exception;

public class InvalidOrderTableToOrder extends IllegalArgumentException {

    public InvalidOrderTableToOrder(final String message) {
        super(message);
    }
}
