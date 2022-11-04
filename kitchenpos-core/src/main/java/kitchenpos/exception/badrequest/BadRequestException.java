package kitchenpos.exception.badrequest;

import kitchenpos.exception.ApplicationException;
import org.springframework.http.HttpStatus;

public abstract class BadRequestException extends ApplicationException {

    protected BadRequestException(final String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
