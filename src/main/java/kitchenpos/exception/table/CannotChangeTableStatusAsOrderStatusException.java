package kitchenpos.exception.table;

public class CannotChangeTableStatusAsOrderStatusException extends RuntimeException {
    private static final String MESSAGE = "조리중이거나 식사중인 테이블의 상태는 변경할 수 없습니다.";

    public CannotChangeTableStatusAsOrderStatusException() {
        super(MESSAGE);
    }
}
