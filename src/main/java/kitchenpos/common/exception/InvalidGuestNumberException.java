package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidGuestNumberException extends HttpException {

    public InvalidGuestNumberException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
