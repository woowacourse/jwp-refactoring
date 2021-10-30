package kitchenpos.exception.tablegroup;

public class CannotUnGroupAsOrderStatusException extends RuntimeException {
    private static final String MESSAGE = "조리중이거나 식사중인 테이블은 그룹에서 해제할 수 없습니다.";

    public CannotUnGroupAsOrderStatusException() {
        super(MESSAGE);
    }
}
