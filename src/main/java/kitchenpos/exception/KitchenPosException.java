package kitchenpos.exception;

import kitchenpos.exception.dto.ExceptionResponse;
import org.springframework.http.HttpStatus;

public class KitchenPosException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ExceptionResponse body;

    public KitchenPosException(HttpStatus httpStatus, ErrorType errorType) {
        this.httpStatus = httpStatus;
        this.body = new ExceptionResponse(errorType.getErrorCode(), errorType.getMessage());
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ExceptionResponse getBody() {
        return body;
    }
}
