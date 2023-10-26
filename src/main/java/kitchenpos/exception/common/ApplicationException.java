package kitchenpos.exception.common;

public abstract class ApplicationException extends RuntimeException {
    public ApplicationException(final String message) {
        super(message);
    }

    abstract public int getErrorCode();
}
