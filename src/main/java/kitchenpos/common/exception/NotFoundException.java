package kitchenpos.common.exception;

public class NotFoundException extends ApplicationException {

    public NotFoundException(final CustomErrorCode errorCode) {
        super(errorCode);
    }
}
