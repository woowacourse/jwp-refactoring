package kitchenpos.menu.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidMenuPriceException extends KitchenposException {

    public InvalidMenuPriceException(String message) {
        super(message);
    }
}
