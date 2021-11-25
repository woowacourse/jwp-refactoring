package kitchenpos.table.exception;

import kitchenpos.common.exception.KitchenposException;

public class TableGroupNotFoundException extends KitchenposException {

    public TableGroupNotFoundException(String message) {
        super(message);
    }
}
