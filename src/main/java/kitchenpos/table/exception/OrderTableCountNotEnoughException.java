package kitchenpos.table.exception;

public class OrderTableCountNotEnoughException extends RuntimeException {

    private static final String MESSAGE = "주문 테이블 개수가 부족합니다.";

    public OrderTableCountNotEnoughException() {
        super(MESSAGE);
    }
}
