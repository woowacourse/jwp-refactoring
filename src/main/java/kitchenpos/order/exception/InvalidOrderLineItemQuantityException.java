package kitchenpos.order.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidOrderLineItemQuantityException extends KitchenposException {

    public InvalidOrderLineItemQuantityException(String message) {
        super(message);
    }
}
