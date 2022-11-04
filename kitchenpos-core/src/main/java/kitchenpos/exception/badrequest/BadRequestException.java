package kitchenpos.exception.badrequest;

import kitchenpos.exception.ApplicationException;

public abstract class BadRequestException extends ApplicationException {

    private static final int BAD_REQUEST_CODE = 400;

    protected BadRequestException(final String message) {
        super(message, BAD_REQUEST_CODE);
    }
}
