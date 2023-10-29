package kitchenpos.exception;

public class InvalidOrderStatusToChangeException extends IllegalArgumentException {

    public InvalidOrderStatusToChangeException(final String message) {
        super(message);
    }
}
