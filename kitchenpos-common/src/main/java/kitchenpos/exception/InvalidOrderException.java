package kitchenpos.exception;

public class InvalidOrderException extends IllegalArgumentException {

    public InvalidOrderException(final String message) {
        super(message);
    }
}
