package kitchenpos.application.exception;

import kitchenpos.domain.exception.KitchenPosException;

public class TableServiceException extends KitchenPosException {

    public TableServiceException(String message) {
        super(message);
    }

    // TODO: 중복로직 존재
    public static class NotExistsOrderTableException extends TableServiceException {

        private static final String NOT_EXISTS_ORDER_TABLE_MESSAGE = "주문 테이블 ID가 존재하지 않습니다. \norder id: ";

        public NotExistsOrderTableException(final Long orderTableId) {
            super(NOT_EXISTS_ORDER_TABLE_MESSAGE + orderTableId);
        }
    }

    public static class ExistsTableGroupException extends TableServiceException {

        private static final String EXISTS_TABLE_GROUP_MESSAGE = "아직 테이블 그룹이 존재합니다.";

        public ExistsTableGroupException() {
            super(EXISTS_TABLE_GROUP_MESSAGE);
        }
    }

    // TODO: 중복 로직 존재
    public static class ExistsNotCompletionOrderException extends TableGroupServiceException {

        private static final String EXISTS_NOT_COMPLETION_ORDER_MESSAGE = "완료되지 않은 주문이 있습니다.";

        public ExistsNotCompletionOrderException() {
            super(EXISTS_NOT_COMPLETION_ORDER_MESSAGE);
        }
    }

    public static class InvalidNumberOfGuestsException extends TableServiceException {

        private static final String INVALID_NUMBER_OF_GUESTS_MESSAGE = "손님은 0명 이상이어야합니다. \nnumber of guest: ";

        public InvalidNumberOfGuestsException(final int numberOfGuests) {
            super(INVALID_NUMBER_OF_GUESTS_MESSAGE + numberOfGuests);
        }
    }

    public static class EmptyTableException extends TableServiceException {

        private static final String EMPTY_TABLE_MESSAGE = "지정한 주문 테이블은 빈 테이블입니다.";

        public EmptyTableException() {
            super(EMPTY_TABLE_MESSAGE);
        }
    }
}
