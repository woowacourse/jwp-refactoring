package kitchenpos.order.domain;

import kitchenpos.order.exception.OrderException;

public class OrderValidator {

    public static void validateOrderLineItemSize(final int OrderLineItemSize, final int foundMenuSize) {
        if (OrderLineItemSize != foundMenuSize) {
            throw new OrderException.NotFoundOrderLineItemMenuExistException();
        }
    }
}
