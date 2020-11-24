package kitchenpos.table.dto;

public class InvalidTableEmptyException extends RuntimeException {
    public InvalidTableEmptyException() {
        super("현재 테이블 상태를 변경할 수 없습니다.");
    }
}
