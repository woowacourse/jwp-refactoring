package kitchenpos.exception.order;

import kitchenpos.exception.common.BadRequestException;

public class CreateOrderBadRequest extends BadRequestException {
    public CreateOrderBadRequest(final String message) {
        super(message);
    }
}
