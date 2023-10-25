package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidOrderStatusException extends HttpException {

    public InvalidOrderStatusException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
