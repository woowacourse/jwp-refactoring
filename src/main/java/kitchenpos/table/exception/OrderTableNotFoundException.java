package kitchenpos.table.exception;

public class OrderTableNotFoundException extends RuntimeException {

    private static final String MESSAGE = "주문 테이블을 찾을 수 없습니다.";

    public OrderTableNotFoundException() {
        super(MESSAGE);
    }
}
