package kitchenpos.domain.exception;

import kitchenpos.common.KitchenPosException;

public class TableGroupException extends KitchenPosException {

    private static final String GROUP_ALREADY_EXIST_MESSAGE = "그룹이 존재해 상태 변경할 수 없습니다.";
    private static final String INVALID_ORDER_TABLE_MESSAGE = "주문 테이블 조건이 맞지 않습니다.";

    public TableGroupException(final String message) {
        super(message);
    }

    public static class GroupAlreadyExistsException extends TableGroupException {

        public GroupAlreadyExistsException() {
            super(GROUP_ALREADY_EXIST_MESSAGE);
        }
    }

    public static class InvalidOrderTablesException extends TableGroupException {

        public InvalidOrderTablesException() {
            super(INVALID_ORDER_TABLE_MESSAGE);
        }
    }
}
