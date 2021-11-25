package kitchenpos.product.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidPriceException extends KitchenposException {

    public InvalidPriceException(String message) {
        super(message);
    }
}
