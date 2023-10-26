package kitchenpos.exception.table;

import kitchenpos.exception.common.BadRequestException;

public class OrderTableIsInTableGroupBadRequest extends BadRequestException {
    private static final String resourceName = "주문 테이블";

    public OrderTableIsInTableGroupBadRequest(final long id) {
        super(resourceName, id);
    }
}
