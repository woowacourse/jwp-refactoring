package kitchenpos.domain;

import kitchenpos.exception.OrderException;

public class OrderValidator {

    public static void validateOrderLineItemSize(final int OrderLineItemSize, final int foundMenuSize) {
        if (OrderLineItemSize != foundMenuSize) {
            throw new OrderException.NotFoundOrderLineItemMenuExistException();
        }
    }

    public static void validateOrderTable(final OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new OrderException.CannotOrderStateByOrderTableEmptyException();
        }
    }
}
