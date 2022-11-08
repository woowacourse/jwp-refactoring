package kitchenpos.tablegroup.exception;

public class OrderTableNotFoundException extends RuntimeException {

    public OrderTableNotFoundException() {
        super("일치하는 주문 테이블이 없습니다.");
    }
}
