package kitchenpos.menu.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidMenuNameException extends KitchenposException {

    public InvalidMenuNameException(String message) {
        super(message);
    }
}
