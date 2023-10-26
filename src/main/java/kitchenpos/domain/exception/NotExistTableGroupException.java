package kitchenpos.domain.exception;

public class NotExistTableGroupException extends RuntimeException {
    public NotExistTableGroupException(final String message) {
        super(message);
    }
}
