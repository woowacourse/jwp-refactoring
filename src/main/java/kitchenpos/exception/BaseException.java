package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseException extends RuntimeException {

    private final HttpStatus status;

    public BaseException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
