package kitchenpos.menu.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidMenuQuantityException extends KitchenposException {

    public InvalidMenuQuantityException(String message) {
        super(message);
    }
}
