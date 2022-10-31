package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApplicationException {

    public NotFoundException(final CustomError errorCode) {
        super(HttpStatus.NOT_FOUND, errorCode);
    }
}
