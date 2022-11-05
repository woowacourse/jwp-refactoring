package kitchenpos.exception;

public class InvalidTableGroupException extends IllegalArgumentException {

    public InvalidTableGroupException(final String message) {
        super(message);
    }
}
