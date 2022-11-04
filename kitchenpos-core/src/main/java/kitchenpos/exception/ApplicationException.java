package kitchenpos.exception;

public abstract class ApplicationException extends RuntimeException {

    private final int httpStatusCode;

    public ApplicationException(final String message, final int httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
