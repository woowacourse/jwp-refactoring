package kitchenpos.exception;

public class TableGroupExistenceException extends RuntimeException {
    public TableGroupExistenceException() {
        super("테이블 그룹이 이미 존재합니다");
    }
}
