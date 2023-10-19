package kitchenpos.domain.exception;

public class OrderException extends KitchenPosException{

    public OrderException(String message) {
        super(message);
    }

    public static class EmptyOrderLineItemsException extends OrderException {

        private static final String EMPTY_ORDER_LINE_ITEMS_MESSAGE = "주문에 주문 항목이 비어있습니다.";

        public EmptyOrderLineItemsException() {
            super(EMPTY_ORDER_LINE_ITEMS_MESSAGE);
        }
    }
}
