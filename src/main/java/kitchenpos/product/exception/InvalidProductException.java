package kitchenpos.product.exception;

import kitchenpos.common.exception.BadRequestException;

public class InvalidProductException extends BadRequestException {

    public InvalidProductException(String message) {
        super(message);
    }
}
