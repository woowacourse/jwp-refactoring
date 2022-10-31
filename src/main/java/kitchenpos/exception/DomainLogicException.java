package kitchenpos.exception;

public class DomainLogicException extends ApplicationException {

    public DomainLogicException(final CustomErrorCode errorCode) {
        super(errorCode);
    }
}
