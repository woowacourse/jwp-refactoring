package kitchenpos.domain.exception;

public abstract class TableGroupException extends KitchenPosException {

    public TableGroupException(String message) {
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
}
