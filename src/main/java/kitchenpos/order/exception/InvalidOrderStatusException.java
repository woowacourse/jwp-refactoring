package kitchenpos.order.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidOrderStatusException extends KitchenposException {

    public InvalidOrderStatusException(String message) {
        super(message);
    }
}
