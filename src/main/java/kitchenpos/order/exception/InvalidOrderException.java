package kitchenpos.order.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidOrderException extends KitchenposException {

    public InvalidOrderException(String message) {
        super(message);
    }
}
