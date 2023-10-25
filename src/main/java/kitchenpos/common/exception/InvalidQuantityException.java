package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidQuantityException extends HttpException {

    public InvalidQuantityException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
