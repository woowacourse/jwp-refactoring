package kitchenpos.exceptions;

public class OrderTableNotEmptyException extends RuntimeException {

    public OrderTableNotEmptyException() {
        super("주문 테이블이 비어있지 않습니다.");
    }
}
