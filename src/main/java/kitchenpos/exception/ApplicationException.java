package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class ApplicationException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final CustomError errorCode;

    public ApplicationException(final CustomError errorCode) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, errorCode);
    }

    public ApplicationException(final HttpStatus httpStatus, final CustomError errorCode) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public CustomError getErrorCode() {
        return errorCode;
    }
}
