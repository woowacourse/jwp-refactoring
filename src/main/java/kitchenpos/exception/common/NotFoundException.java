package kitchenpos.exception.common;

public abstract class NotFoundException extends ApplicationException {
    private static final String message = "해당 자원을 찾을 수 없습니다: ";

    public NotFoundException(final String resourceName, final long id) {
        super(message + resourceName + " [id=" + id + "]");
    }

    @Override
    public int getErrorCode() {
        return 404;
    }

}
