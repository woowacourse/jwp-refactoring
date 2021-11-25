package kitchenpos.order.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidOrderLineItemException extends KitchenposException {

    public InvalidOrderLineItemException(String message) {
        super(message);
    }
}
