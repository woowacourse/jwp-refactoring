package kitchenpos.order.exception;

import kitchenpos.common.exception.KitchenposException;

public class OrderAlreadyCompletionException extends KitchenposException {

    public OrderAlreadyCompletionException(String message) {
        super(message);
    }
}
