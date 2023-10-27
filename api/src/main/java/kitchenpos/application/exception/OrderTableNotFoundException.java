package kitchenpos.application.exception;

public class OrderTableNotFoundException extends IllegalArgumentException {

    public OrderTableNotFoundException() {
        super("지정한 주문 테이블을 찾을 수 없습니다.");
    }
}
