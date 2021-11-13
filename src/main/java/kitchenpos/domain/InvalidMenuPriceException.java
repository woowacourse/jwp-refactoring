package kitchenpos.domain;

import kitchenpos.exception.KitchenposException;

public class InvalidMenuPriceException extends KitchenposException {

    public InvalidMenuPriceException(String message) {
        super(message);
    }
}
