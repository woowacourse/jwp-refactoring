package kitchenpos.domain.exception;

public class InvalidTableGroupException extends RuntimeException {
    public InvalidTableGroupException(final String message) {
        super(message);
    }
}
