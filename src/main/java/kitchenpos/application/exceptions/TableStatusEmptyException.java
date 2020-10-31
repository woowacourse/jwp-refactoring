package kitchenpos.application.exceptions;

public class TableStatusEmptyException extends RuntimeException {
    public TableStatusEmptyException() {
        super("테이블 상태가 empty입니다.");
    }
}
