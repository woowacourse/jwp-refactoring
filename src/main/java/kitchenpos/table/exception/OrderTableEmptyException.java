package kitchenpos.table.exception;

import kitchenpos.common.exception.KitchenposException;

public class OrderTableEmptyException extends KitchenposException {

    public OrderTableEmptyException(String message) {
        super(message);
    }
}
