package kitchenpos.menugroup.exception;

import kitchenpos.common.exception.KitchenposException;

public class InvalidMenuGroupNameException extends KitchenposException {

    public InvalidMenuGroupNameException(String message) {
        super(message);
    }
}
