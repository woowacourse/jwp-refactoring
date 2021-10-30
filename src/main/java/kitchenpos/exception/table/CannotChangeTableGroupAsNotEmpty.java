package kitchenpos.exception.table;

public class CannotChangeTableGroupAsNotEmpty extends RuntimeException {
    private static final String MESSAGE = "테이블 그룹은 테이블이 주문을 받을 수 없는 상태여야 바꿀 수 있습니다.";

    public CannotChangeTableGroupAsNotEmpty() {
        super(MESSAGE);
    }
}
