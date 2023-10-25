package kitchenpos.common.exception;

import org.springframework.http.HttpStatus;

public class InvalidUnGroupException extends HttpException {

    public InvalidUnGroupException(final String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
