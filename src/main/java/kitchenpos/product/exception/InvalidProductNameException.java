package kitchenpos.product.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidProductNameException extends KitchenposException {

    public InvalidProductNameException(String message) {
        super(message);
    }
}
