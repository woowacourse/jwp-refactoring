package table.exception;

import common.exception.BadRequestException;

public class OrderTableIsInOtherTableGroupBadRequest extends BadRequestException {
    private static final String resourceName = "주문 테이블";

    public OrderTableIsInOtherTableGroupBadRequest(final long id) {
        super(resourceName, id);
    }
}
