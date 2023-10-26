package kitchenpos.exception.order;

import kitchenpos.exception.common.NotFoundException;

public class OrderNotFoundException extends NotFoundException {
    private static final String RESOURCE = "주문";

    public OrderNotFoundException(final Long orderId) {
        super(RESOURCE, orderId);
    }

    public OrderNotFoundException() {
        super(RESOURCE);
    }
}
