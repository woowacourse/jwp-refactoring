package kitchenpos.menu.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidMenuException extends KitchenposException {

    public InvalidMenuException(String message) {
        super(message);
    }
}
