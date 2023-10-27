package kitchenpos.application.exception;

import kitchenpos.common.KitchenPosException;

public class TableGroupAppException extends KitchenPosException {

    private static final String NOT_FOUND_TABLE_GROUP_MESSAGE = "테이블 그룹을 찾을 수 없습니다. id = ";
    private static final String EMPTY_ORDER_TABLE_MESSAGE = "주문 테이블이 비어있습니다.";
    private static final String ORDER_TABLE_MISMATCH_MESSAGE = "그룹화 하려는 주문테이블 개수가 맞지 않습니다.";

    public TableGroupAppException(final String message) {
        super(message);
    }

    public static class NotFoundTableGroupException extends TableGroupAppException {

        public NotFoundTableGroupException(final Long tableGroupId) {
            super(NOT_FOUND_TABLE_GROUP_MESSAGE + tableGroupId);
        }
    }

    public static class EmptyOrderTablesCreateTableGroupException extends TableGroupAppException {

        public EmptyOrderTablesCreateTableGroupException() {
            super(EMPTY_ORDER_TABLE_MESSAGE);
        }
    }

    public static class OrderTableCountMismatchException extends TableGroupAppException {

        public OrderTableCountMismatchException() {
            super(ORDER_TABLE_MISMATCH_MESSAGE);
        }
    }
}
