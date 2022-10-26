package kitchenpos.exception;

public class ApplicationException extends RuntimeException {

    private final CustomErrorCode errorCode;

    public ApplicationException(CustomErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
