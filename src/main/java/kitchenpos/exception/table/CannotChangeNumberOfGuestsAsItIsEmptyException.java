package kitchenpos.exception.table;

public class CannotChangeNumberOfGuestsAsItIsEmptyException extends RuntimeException {
    private static final String MESSAGE = "주문을 받을 수 없는 테이블에는 방문한 손님 수를 바꿀 수 없습니다.";

    public CannotChangeNumberOfGuestsAsItIsEmptyException() {
        super(MESSAGE);
    }
}
