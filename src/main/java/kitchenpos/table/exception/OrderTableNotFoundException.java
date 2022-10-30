package kitchenpos.table.exception;

public class OrderTableNotFoundException extends RuntimeException {

    public OrderTableNotFoundException() {
        super("주문 테이블을 찾을 수 없습니다.");
    }
}
