package kitchenpos.domain.orertable.exception;


import kitchenpos.exception.CustomException;

public class TableGroupException extends CustomException {

    public TableGroupException(final String message) {
        super(message);
    }

    public static class NotFoundOrderTableExistException extends TableGroupException {

        public NotFoundOrderTableExistException() {
            super("주문 테이블 목록 중 존재하지 않는 주문 테이블이 있습니다.");
        }
    }

    public static class CannotCreateTableGroupStateException extends TableGroupException {

        public CannotCreateTableGroupStateException() {
            super("주문 테이블이 빈 상태가 아니거나 테이블 그룹이 존재하지 않을 때 테이블 그룹을 생성할 수 없습니다.");
        }
    }

    public static class CannotUngroupStateByOrderStatusException extends TableGroupException {

        public CannotUngroupStateByOrderStatusException() {
            super("주문 테이블의 주문 상태가 조리중이거나 식사중일 때 테이블 그룹을 해제할 수 없습니다.");
        }
    }
}
