package kitchenpos.exception;

public class TableCannotChangeEmptyException extends RuntimeException {
    public TableCannotChangeEmptyException() {
        super("테이블의 빈 상태를 변경할 수 없습니다");
    }
}
