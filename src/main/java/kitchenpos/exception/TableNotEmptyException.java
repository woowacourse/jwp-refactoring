package kitchenpos.exception;

public class TableNotEmptyException extends RuntimeException {
    public TableNotEmptyException() {
        super("테이블이 비어있지 않습니다");
    }
}
