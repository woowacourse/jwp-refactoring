package kitchenpos.menugroup.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidNameException extends KitchenposException {

    public InvalidNameException(String message) {
        super(message);
    }
}
