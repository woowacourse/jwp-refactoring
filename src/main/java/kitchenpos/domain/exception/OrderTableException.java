package kitchenpos.domain.exception;

import kitchenpos.common.KitchenPosException;

public class OrderTableException extends KitchenPosException {

    private static final String NOT_ENOUGH_GUEST_MESSAGE = "주문 테이블 최소 인원이 충족되지 않았습니다.";

    public OrderTableException(final String message) {
        super(message);
    }

    public static class NotEnoughGuestsException extends OrderTableException {

        public NotEnoughGuestsException() {
            super(NOT_ENOUGH_GUEST_MESSAGE);
        }
    }
}
