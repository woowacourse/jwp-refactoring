package kitchenpos.product.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidProductPriceException extends KitchenposException {

    public InvalidProductPriceException(String message) {
        super(message);
    }
}
