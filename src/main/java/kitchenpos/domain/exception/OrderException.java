package kitchenpos.domain.exception;

import kitchenpos.application.exception.OrderAppException;
import kitchenpos.common.KitchenPosException;

public class OrderException extends KitchenPosException {

    private static final String EMPTY_ORDER_TABLE_FOR_CREATE_ORDER_MESSAGE = "주문 테이블이 비어있어서 주문 생성이 되지 않습니다.";
    private static final String EMPTY_ORDER_LINE_ITEM_FOR_CREATE_ORDER_MESSAGE = "주문 상품들이 비어있어서 주문 생성이 되지 않습니다.";

    public OrderException(final String message) {
        super(message);
    }

    public static class EmptyOrderTableException extends OrderAppException {

        public EmptyOrderTableException() {
            super(EMPTY_ORDER_TABLE_FOR_CREATE_ORDER_MESSAGE);
        }
    }

    public static class EmptyOrderLineItemsException extends OrderAppException {

        public EmptyOrderLineItemsException() {
            super(EMPTY_ORDER_LINE_ITEM_FOR_CREATE_ORDER_MESSAGE);
        }
    }
}
