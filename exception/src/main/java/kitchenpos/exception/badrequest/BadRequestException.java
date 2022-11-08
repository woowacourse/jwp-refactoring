package kitchenpos.exception.badrequest;

import kitchenpos.exception.KitchenPosException;

public class BadRequestException extends KitchenPosException {
    private static final String DEFAULT_MESSAGE = "잘못된 요청입니다";

    public BadRequestException() {
        super(DEFAULT_MESSAGE);
    }

    public BadRequestException(final String message) {
        super(message);
    }

}
