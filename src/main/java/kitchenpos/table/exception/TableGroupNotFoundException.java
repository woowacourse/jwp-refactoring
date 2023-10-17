package kitchenpos.table.exception;

public class TableGroupNotFoundException extends RuntimeException {

    private static final String MESSAGE = "테이블 그룹을 찾을 수 없습니다.";

    public TableGroupNotFoundException() {
        super(MESSAGE);
    }
}
