package kitchenpos.domain;

import kitchenpos.exception.OrderException;

public class OrderValidator {

    public static void validateOrderLineItemSize(final int OrderLineItemSize, final int foundMenuSize) {
        if (OrderLineItemSize != foundMenuSize) {
            throw new OrderException.NotFoundOrderLineItemMenuExistException();
        }
    }
}
