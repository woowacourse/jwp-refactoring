package kitchenpos.menu.exception;

import kitchenpos.common.exception.BadRequestException;

public class InvalidMenuException extends BadRequestException {

    public InvalidMenuException(String message) {
        super(message);
    }
}
