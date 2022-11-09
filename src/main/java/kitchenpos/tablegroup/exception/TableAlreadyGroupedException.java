package kitchenpos.tablegroup.exception;

public class TableAlreadyGroupedException extends IllegalArgumentException {

    private static final String MESSAGE = "이미 Table Group에 속해있는 테이블로는 Table Group을 생성할 수 없습니다.";

    public TableAlreadyGroupedException() {
        super(MESSAGE);
    }
}
