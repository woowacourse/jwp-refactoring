package kitchenpos.order.exception;

import kitchenpos.common.CreateFailException;

public class TableEmptyException extends CreateFailException {
    public TableEmptyException(String message) {
        super(message);
    }
}
