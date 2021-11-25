package kitchenpos.menu.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidMenuProductException extends KitchenposException {

    public InvalidMenuProductException(String message) {
        super(message);
    }
}
