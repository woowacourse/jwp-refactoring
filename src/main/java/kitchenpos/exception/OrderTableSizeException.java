package kitchenpos.exception;

public class OrderTableSizeException extends IllegalArgumentException {

    public OrderTableSizeException() {
        super("주문 테이블의 사이즈가 올바르지 않습니다");
    }
}
