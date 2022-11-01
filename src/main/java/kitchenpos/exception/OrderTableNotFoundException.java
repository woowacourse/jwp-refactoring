package kitchenpos.exception;

public class OrderTableNotFoundException extends RuntimeException {
    public OrderTableNotFoundException() {
        super("주문 테이블이 서버에 저장되어 있지 않습니다.");
    }
}
