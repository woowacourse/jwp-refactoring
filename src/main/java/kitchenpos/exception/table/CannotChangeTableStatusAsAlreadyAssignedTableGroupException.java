package kitchenpos.exception.table;

public class CannotChangeTableStatusAsAlreadyAssignedTableGroupException extends RuntimeException {
    private static final String MESSAGE = "테이블 그룹에 등록되어 있어 상태를 변경시킬 수 없습니다.";

    public CannotChangeTableStatusAsAlreadyAssignedTableGroupException() {
        super(MESSAGE);
    }
}
