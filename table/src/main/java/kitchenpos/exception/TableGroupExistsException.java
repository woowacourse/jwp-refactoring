package kitchenpos.exception;

public class TableGroupExistsException extends RuntimeException {

    private static final String MESSAGE = "이미 그룹이 지정된 테이블입니다.";

    public TableGroupExistsException() {
        super(MESSAGE);
    }
}
