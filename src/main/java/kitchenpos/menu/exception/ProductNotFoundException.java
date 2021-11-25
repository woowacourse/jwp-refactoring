package kitchenpos.menu.exception;

import kitchenpos.common.exception.KitchenposException;

public class ProductNotFoundException extends KitchenposException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}
