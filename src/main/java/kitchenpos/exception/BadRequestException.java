package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends KitchenPosException {
    public static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    public BadRequestException(ErrorType errorType) {
        super(BAD_REQUEST, errorType);
    }
}
