package kitchenpos.application.exception;

import kitchenpos.common.KitchenPosException;

public class OrderTableAppException extends KitchenPosException {

    private static final String NOT_FOUND_ORDER_TABLE_MESSAGE = "주문 테이블을 찾을 수 없습니다.";

    public OrderTableAppException(final String message) {
        super(message);
    }

    public static class NotFoundOrderTableException extends OrderTableAppException {

        public NotFoundOrderTableException() {
            super(NOT_FOUND_ORDER_TABLE_MESSAGE);
        }
    }
}
