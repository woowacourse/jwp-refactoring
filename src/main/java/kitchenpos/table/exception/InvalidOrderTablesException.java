package kitchenpos.table.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidOrderTablesException extends KitchenposException {

    public InvalidOrderTablesException(String message) {
        super(message);
    }
}
