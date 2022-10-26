package kitchenpos.common.exception;

public class DomainLogicException extends ApplicationException {

    public DomainLogicException(final CustomErrorCode errorCode) {
        super(errorCode);
    }
}
