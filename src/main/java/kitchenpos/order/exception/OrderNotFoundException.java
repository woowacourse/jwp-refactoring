package kitchenpos.order.exception;

import kitchenpos.common.exception.KitchenposException;

public class OrderNotFoundException extends KitchenposException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}
