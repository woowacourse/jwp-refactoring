package kitchenpos.exception;

public class InvalidOrderToChangeEmptyException extends IllegalArgumentException {

    public InvalidOrderToChangeEmptyException(final String message) {
        super(message);
    }
}
