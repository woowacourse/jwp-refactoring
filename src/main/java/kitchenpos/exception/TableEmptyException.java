package kitchenpos.exception;

public class TableEmptyException extends RuntimeException {
    public TableEmptyException() {
        super("테이블이 비어있습니다");
    }
}
