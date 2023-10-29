package common.exception;

public abstract class BadRequestException extends ApplicationException {
    private static final String MESSAGE = "잘못된 요청입니다.: ";

    public BadRequestException(final String resourceName, final long id) {
        super(MESSAGE + resourceName + " [id=" + id + "]");
    }

    public BadRequestException(final String message) {
        super(MESSAGE + message);
    }

    @Override
    public int getErrorCode() {
        return 400;
    }
}
