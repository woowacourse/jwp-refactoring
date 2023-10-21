package kitchenpos.domain.exception;

public abstract class TableGroupException extends KitchenPosException {

    public TableGroupException(final String message) {
        super(message);
    }

    public static class CannotAssignOrderTableException extends TableGroupException {

        private static final String CANNOT_ASSIGN_ORDER_TABLE_MASSAGE = "주문 테이블을 테이블 그룹에 지정할 수 없습니다.";

        public CannotAssignOrderTableException() {
            super(CANNOT_ASSIGN_ORDER_TABLE_MASSAGE);
        }
    }

    public static class InsufficientOrderTableSizeException extends TableGroupException {

        private static final String INSUFFICIENT_ORDER_TABLE_SIZE_MESSAGE = "주문 테이블의 개수는 2개 이상이어야합니다.";

        public InsufficientOrderTableSizeException() {
            super(INSUFFICIENT_ORDER_TABLE_SIZE_MESSAGE);
        }
    }

    public static class NotExistsTableGroupException extends TableGroupException {

        private static final String NOT_EXISTS_TABLE_GROUP_MESSAGE = "테이블 그룹이 없습니다. 현재 테이블 그룹: ";

        public NotExistsTableGroupException(final Long tableGroupId) {
            super(NOT_EXISTS_TABLE_GROUP_MESSAGE + tableGroupId);
        }
    }

    public static class ExistsNotCompletionOrderException extends TableGroupException {

        private static final String EXISTS_NOT_COMPLETION_ORDER_MESSAGE = "완료되지 않은 주문이 있습니다.";

        public ExistsNotCompletionOrderException() {
            super(EXISTS_NOT_COMPLETION_ORDER_MESSAGE);
        }
    }
}
