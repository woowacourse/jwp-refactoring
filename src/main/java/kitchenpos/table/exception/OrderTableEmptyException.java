package kitchenpos.table.exception;

public class OrderTableEmptyException extends RuntimeException {

    private static final String MESSAGE = "빈 테이블입니다.";

    public OrderTableEmptyException() {
        super(MESSAGE);
    }
}
