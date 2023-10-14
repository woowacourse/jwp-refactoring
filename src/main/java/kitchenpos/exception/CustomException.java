package kitchenpos.exception;

public abstract class CustomException extends RuntimeException {

    private static final String EXCEPTION_PREFIX = "[ERROR] ";

    public CustomException(final String message) {
        super(EXCEPTION_PREFIX + message);
    }
}
