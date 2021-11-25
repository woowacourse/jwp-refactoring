package kitchenpos.order.exception;

import kitchenpos.common.exception.KitchenposException;

public class OrderLineItemDuplicateException extends KitchenposException {

    public OrderLineItemDuplicateException(String message) {
        super(message);
    }
}
