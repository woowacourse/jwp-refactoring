package kitchenpos.tablegroup.exception;

import kitchenpos.common.exception.KitchenposException;

public class OrderTableNotFoundException extends KitchenposException {

    public OrderTableNotFoundException(String message) {
        super(message);
    }
}
