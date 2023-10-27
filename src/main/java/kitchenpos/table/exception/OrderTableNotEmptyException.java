package kitchenpos.table.exception;

public class OrderTableNotEmptyException extends RuntimeException {

    private static final String MESSAGE = "빈 테이블이 아닙니다.";

    public OrderTableNotEmptyException() {
        super(MESSAGE);
    }
}
