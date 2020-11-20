package kitchenpos.exception;

public class TableNotExistenceException extends RuntimeException {
    public TableNotExistenceException() {
        super("테이블이 존재하지 않습니다");
    }
}
