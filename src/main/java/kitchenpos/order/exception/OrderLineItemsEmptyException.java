package kitchenpos.order.exception;

import kitchenpos.common.exception.KitchenposException;

public class OrderLineItemsEmptyException extends KitchenposException {

    public OrderLineItemsEmptyException(String message) {
        super(message);
    }
}
