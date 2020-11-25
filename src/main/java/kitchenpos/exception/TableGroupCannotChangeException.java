package kitchenpos.exception;

public class TableGroupCannotChangeException extends RuntimeException {
    public TableGroupCannotChangeException() {
        super("테이블 그룹을 변경할 수 없습니다");
    }
}
