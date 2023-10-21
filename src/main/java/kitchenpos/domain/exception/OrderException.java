package kitchenpos.domain.exception;

public abstract class OrderException extends KitchenPosException {

    public OrderException(final String message) {
        super(message);
    }

    public static class EmptyOrderLineItemsException extends OrderException {

        private static final String EMPTY_ORDER_LINE_ITEMS_MESSAGE = "주문에 주문 항목이 비어있습니다.";

        public EmptyOrderLineItemsException() {
            super(EMPTY_ORDER_LINE_ITEMS_MESSAGE);
        }
    }

    public static class CompletionOrderException extends OrderException {

        private static final String COMPLETION_ORDER_MESSAGE = "이미 완료된 주문입니다.";

        public CompletionOrderException() {
            super(COMPLETION_ORDER_MESSAGE);
        }
    }

    public static class NotExistsOrderException extends OrderException {

        private static final String NOT_EXISTS_ORDER_MESSAGE = "주문 ID가 존재하지 않습니다. \norder id: ";

        public NotExistsOrderException(final Long orderId) {
            super(NOT_EXISTS_ORDER_MESSAGE + orderId);
        }
    }

    public static class NotExistsMenuException extends OrderException {

        private static final String NOT_EXISTS_MENU_MESSAGE = "주문에 id가 없는 메뉴가 포함되어 있습니다.";

        public NotExistsMenuException() {
            super(NOT_EXISTS_MENU_MESSAGE);
        }
    }

    public static class EmptyOrderTableException extends OrderException {

        private static final String EMPTY_ORDER_TABLE_MESSAGE = "주문에 포함된 주문 테이블이 빈 테이블입니다. \norder table id: ";

        public EmptyOrderTableException(final Long orderTableId) {
            super(EMPTY_ORDER_TABLE_MESSAGE + orderTableId);
        }
    }
}
