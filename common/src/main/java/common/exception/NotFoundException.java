package common.exception;

public abstract class NotFoundException extends ApplicationException {
    private static final String MESSAGE = "해당 자원을 찾을 수 없습니다: ";

    public NotFoundException(final String resourceName, final long id) {
        super(MESSAGE + resourceName + " [id=" + id + "]");
    }

    public NotFoundException(final String resourceName) {
        super(MESSAGE + resourceName);
    }

    @Override
    public int getErrorCode() {
        return 404;
    }

}
