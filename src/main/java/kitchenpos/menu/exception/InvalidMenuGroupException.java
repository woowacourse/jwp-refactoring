package kitchenpos.menu.exception;

import kitchenpos.common.exception.BadRequestException;

public class InvalidMenuGroupException extends BadRequestException {

    public InvalidMenuGroupException(String message) {
        super(message);
    }
}
