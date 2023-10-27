package kitchenpos.application.exception;

public class InvalidOrderStatusToChangeException extends IllegalArgumentException {

    public InvalidOrderStatusToChangeException(final String message) {
        super(message);
    }
}
