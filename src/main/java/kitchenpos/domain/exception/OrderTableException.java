package kitchenpos.domain.exception;

public abstract class OrderTableException extends KitchenPosException {

    public OrderTableException(String message) {
        super(message);
    }

    public static class InvalidNumberOfGuestsException extends OrderTableException {

        private static final String INVALID_NUMBER_OF_GUESTS_MESSAGE = "손님은 0명 이상이어야합니다. \nnumber of guest: ";

        public InvalidNumberOfGuestsException(final int numberOfGuests) {
            super(INVALID_NUMBER_OF_GUESTS_MESSAGE + numberOfGuests);
        }
    }

    public static class ExistsTableGroupException extends OrderTableException {

        private static final String EXISTS_TABLE_GROUP_MESSAGE = "아직 테이블 그룹이 존재합니다.";

        public ExistsTableGroupException() {
            super(EXISTS_TABLE_GROUP_MESSAGE);
        }
    }

    public static class EmptyTableException extends OrderTableException {

        private static final String EMPTY_TABLE_MESSAGE = "지정한 주문 테이블은 빈 테이블입니다.";

        public EmptyTableException() {
            super(EMPTY_TABLE_MESSAGE);
        }
    }

    public static class NotExistsOrderTableException extends OrderTableException {

        private static final String NOT_EXISTS_ORDER_TABLE_MESSAGE = "주문에 포함된 주문 테이블이 존재하지 않습니다.";

        public NotExistsOrderTableException() {
            super(NOT_EXISTS_ORDER_TABLE_MESSAGE);
        }
    }

    public static class ExistsNotCompletionOrderException extends OrderTableException {

        private static final String EXISTS_NOT_COMPLETION_ORDER_MESSAGE = "완료되지 않은 주문이 있습니다. 완료되지 않은 주문 테이블 번호: ";

        public ExistsNotCompletionOrderException(final Long orderTableId) {
            super(EXISTS_NOT_COMPLETION_ORDER_MESSAGE + orderTableId);
        }
    }
}
