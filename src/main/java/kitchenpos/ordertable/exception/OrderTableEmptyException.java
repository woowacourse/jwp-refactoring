package kitchenpos.ordertable.exception;

public class OrderTableEmptyException extends RuntimeException {

    public OrderTableEmptyException() {
        super("주문 테이블이 비어있습니다.");
    }
}
