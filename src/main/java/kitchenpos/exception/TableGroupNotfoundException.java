package kitchenpos.exception;

public class TableGroupNotfoundException extends RuntimeException {
    public TableGroupNotfoundException() {
        super("테이블 그룹이 저장되어 있지 않습니다.");
    }
}
