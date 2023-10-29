package table.exception;

import common.exception.BadRequestException;

public class OrderIsNotCompletedBadRequestException extends BadRequestException {
    private static final String RESOURCE_NAME = "주문 테이블";

    public OrderIsNotCompletedBadRequestException(final long orderTableId) {
        super(RESOURCE_NAME, orderTableId);
    }
}
