package kitchenpos.exception;

import org.springframework.http.HttpStatus;

public class DomainLogicException extends ApplicationException {

    public DomainLogicException(final CustomError errorCode) {
        super(HttpStatus.BAD_REQUEST, errorCode);
    }
}
