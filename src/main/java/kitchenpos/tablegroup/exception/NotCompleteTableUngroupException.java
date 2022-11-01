package kitchenpos.tablegroup.exception;

public class NotCompleteTableUngroupException extends IllegalArgumentException {

    private static final String MESSAGE = "조리중이거나 식사중인 테이블이 포함된 Table Group은 그룹 해제 할 수 없습니다.";

    public NotCompleteTableUngroupException() {
        super(MESSAGE);
    }
}
