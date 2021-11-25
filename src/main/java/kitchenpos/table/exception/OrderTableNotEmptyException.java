package kitchenpos.table.exception;

import kitchenpos.common.exception.KitchenposException;

public class OrderTableNotEmptyException extends KitchenposException {

    public OrderTableNotEmptyException(String message) {
        super(message);
    }
}
