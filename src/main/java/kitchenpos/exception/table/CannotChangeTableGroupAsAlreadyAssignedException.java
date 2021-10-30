package kitchenpos.exception.table;

public class CannotChangeTableGroupAsAlreadyAssignedException extends RuntimeException {
    private static final String MESSAGE = "이미 테이블 그룹에 등록되어 있어 다른 테이블 그룹에 배정할 수 없습니다.";

    public CannotChangeTableGroupAsAlreadyAssignedException() {
        super(MESSAGE);
    }
}
