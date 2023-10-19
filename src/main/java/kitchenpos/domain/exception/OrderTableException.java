package kitchenpos.domain.exception;

public class OrderTableException extends KitchenPosException{

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
}
