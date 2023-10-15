package kitchenpos.exception;

public class TableGroupInvalidSizeException extends RuntimeException {

    public TableGroupInvalidSizeException() {
        super("테이블 그룹이 1개 이하입니다.");
    }
}
