package kitchenpos.menu.exception;

import kitchenpos.common.exception.KitchenposException;

public class MenuGroupNotFoundException extends KitchenposException {

    public MenuGroupNotFoundException(String message) {
        super(message);
    }
}
