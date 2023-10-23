package kitchenpos.application.exception;

import kitchenpos.common.KitchenPosException;

public class OrderLineItemAppException extends KitchenPosException {

    private static final String EMPTY_ORDER_LINE_ITEM_MESSAGE = "주문 생성할 주문 상품이 없습니다.";

    public OrderLineItemAppException(final String message) {
        super(message);
    }

    public static class EmptyOrderLineItemException extends OrderLineItemAppException {

        public EmptyOrderLineItemException() {
            super(EMPTY_ORDER_LINE_ITEM_MESSAGE);
        }
    }
}
