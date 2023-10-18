package kitchenpos.application.exception;

import kitchenpos.domain.exception.KitchenPosException;

public class TableGroupServiceException extends KitchenPosException {

    public TableGroupServiceException(final String message) {
        super(message);
    }

    public static class InsufficientOrderTableSizeException extends TableGroupServiceException {

        private static final String INSUFFICIENT_ORDER_TABLE_SIZE_MESSAGE = "주문 테이블의 개수는 2개 이상이어야합니다.";

        public InsufficientOrderTableSizeException() {
            super(INSUFFICIENT_ORDER_TABLE_SIZE_MESSAGE);
        }
    }

    public static class NotExistsOrderTableException extends TableGroupServiceException {

        private static final String NOT_EXISTS_ORDER_TABLE_MESSAGE = "테이블 그룹에 id가 없는 주문 테이블이 포함되어 있습니다.";

        public NotExistsOrderTableException() {
            super(NOT_EXISTS_ORDER_TABLE_MESSAGE);
        }
    }

    public static class CannotAssignOrderTableException extends TableGroupServiceException {

        private static final String CANNOT_ASSIGN_ORDER_TABLE_MASSAGE = "주문 테이블을 테이블 그룹에 지정할 수 없습니다. \norder table id: ";

        public CannotAssignOrderTableException(final Long orderTableId) {
            super(CANNOT_ASSIGN_ORDER_TABLE_MASSAGE + orderTableId);
        }
    }

    public static class ExistsNotCompletionOrderException extends TableGroupServiceException {

        private static final String EXISTS_NOT_COMPLETION_ORDER_MESSAGE = "완료되지 않은 주문이 있습니다.";

        public ExistsNotCompletionOrderException() {
            super(EXISTS_NOT_COMPLETION_ORDER_MESSAGE);
        }
    }
}
