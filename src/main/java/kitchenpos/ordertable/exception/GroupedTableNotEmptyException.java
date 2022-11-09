package kitchenpos.ordertable.exception;

public class GroupedTableNotEmptyException extends IllegalArgumentException {

    private static final String MESSAGE = "empty 상태가 아닌 테이블로는 Table Group을 생성할 수 없습니다.";

    public GroupedTableNotEmptyException() {
        super(MESSAGE);
    }
}
