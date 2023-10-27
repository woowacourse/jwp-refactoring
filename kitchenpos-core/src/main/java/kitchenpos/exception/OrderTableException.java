package kitchenpos.exception;

import kitchenpos.common.KitchenPosException;

public class OrderTableException extends KitchenPosException {

    private static final String NOT_ENOUGH_GUEST_MESSAGE = "주문 테이블 최소 인원이 충족되지 않았습니다.";
    private static final String NOT_FOUND_ORDER_TABLE_MESSAGE = "주문 테이블을 찾을 수 없습니다.";
    private static final String EMPTY_ORDER_TABLE_MESSAGE = "주문 테이블이 비어있는 상태입니다.";

    public OrderTableException(final String message) {
        super(message);
    }

    public static class NotEnoughGuestsException extends OrderTableException {

        public NotEnoughGuestsException() {
            super(NOT_ENOUGH_GUEST_MESSAGE);
        }
    }

    public static class NotFoundOrderTableException extends OrderTableException {

        public NotFoundOrderTableException() {
            super(NOT_FOUND_ORDER_TABLE_MESSAGE);
        }
    }

    public static class EmptyTableException extends OrderTableException {

        public EmptyTableException() {
            super(EMPTY_ORDER_TABLE_MESSAGE);
        }
    }
}
