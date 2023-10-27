package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidPriceException extends HttpException {

    public InvalidPriceException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
