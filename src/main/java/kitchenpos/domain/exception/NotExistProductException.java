package kitchenpos.domain.exception;

public class NotExistProductException extends RuntimeException {
    public NotExistProductException(final String message) {
        super(message);
    }
}
