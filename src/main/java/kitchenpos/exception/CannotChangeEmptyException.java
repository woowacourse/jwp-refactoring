package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class CannotChangeEmptyException extends HttpException {

    public CannotChangeEmptyException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
