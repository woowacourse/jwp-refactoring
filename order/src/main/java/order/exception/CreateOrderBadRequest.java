package order.exception;

import common.exception.BadRequestException;

public class CreateOrderBadRequest extends BadRequestException {
    public CreateOrderBadRequest(final String message) {
        super(message);
    }
}
