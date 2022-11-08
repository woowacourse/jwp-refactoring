package kitchenpos.order.exception;

import kitchenpos.common.exception.BadRequestException;

public class InvalidOrderException extends BadRequestException {

    public InvalidOrderException(String message) {
        super(message);
    }
}
