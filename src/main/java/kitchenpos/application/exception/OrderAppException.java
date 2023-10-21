package kitchenpos.application.exception;

import kitchenpos.common.KitchenPosException;

public class OrderAppException extends KitchenPosException {

    private static final String EMPTY_ORDER_TABLE_FOR_CREATE_ORDER_MESSAGE = "주문 테이블이 비어있어서 주문 생성이 되지 않습니다.";
    private static final String NOT_FOUND_ORDER_MESSAGE = "주문을 찾을 수 없습니다. id = ";
    private static final String ALREADY_COMPLETED_MESSAGE = "주문이 이미 완료된 상태입니다. id = ";

    public OrderAppException(final String message) {
        super(message);
    }

    public static class EmptyOrderTableException extends OrderAppException {

        public EmptyOrderTableException() {
            super(EMPTY_ORDER_TABLE_FOR_CREATE_ORDER_MESSAGE);
        }
    }

    public static class NotFoundOrderException extends OrderAppException {

        public NotFoundOrderException(final Long orderId) {
            super(NOT_FOUND_ORDER_MESSAGE + orderId);
        }
    }

    public static class OrderAlreadyCompletedException extends OrderAppException {

        public OrderAlreadyCompletedException(final Long orderId) {
            super(ALREADY_COMPLETED_MESSAGE + orderId);
        }
    }
}
