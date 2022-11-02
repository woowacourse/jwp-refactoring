package kitchenpos.exception;

public class TableGroupNullException extends RuntimeException {

    public TableGroupNullException() {
        super("테이블 그룹 정보가 없습니다.");
    }
}
