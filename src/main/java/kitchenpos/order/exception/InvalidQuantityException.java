package kitchenpos.order.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidQuantityException extends KitchenposException {

    public InvalidQuantityException(String message) {
        super(message);
    }
}
