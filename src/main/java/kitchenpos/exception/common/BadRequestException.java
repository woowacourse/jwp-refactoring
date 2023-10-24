package kitchenpos.exception.common;

public abstract class BadRequestException extends ApplicationException {
    private static final String MESSAGE = "잘못된 요청입니다.: ";

    public BadRequestException(final String resourceName, final long id) {
        super(MESSAGE + resourceName + " [id=" + id + "]");
    }

    @Override
    public int getErrorCode() {
        return 400;
    }
}
