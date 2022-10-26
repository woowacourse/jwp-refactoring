package kitchenpos.common.exception;

public class ApplicationException extends RuntimeException {

    private final CustomErrorCode errorCode;

    public ApplicationException(final CustomErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
