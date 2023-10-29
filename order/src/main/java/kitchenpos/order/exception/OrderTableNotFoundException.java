package kitchenpos.order.exception;

import kitchenpos.common.exception.NotFoundException;

public class OrderTableNotFoundException extends NotFoundException {
    private static final String RESOURCE = "주문 테이블";

    public OrderTableNotFoundException(final Long orderTableId) {
        super(RESOURCE, orderTableId);
    }
}
