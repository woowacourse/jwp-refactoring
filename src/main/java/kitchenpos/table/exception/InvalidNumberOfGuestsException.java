package kitchenpos.table.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidNumberOfGuestsException extends KitchenposException {

    public InvalidNumberOfGuestsException(String message) {
        super(message);
    }
}
