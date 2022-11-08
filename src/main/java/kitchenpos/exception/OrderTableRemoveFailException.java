package kitchenpos.exception;

public class OrderTableRemoveFailException extends RuntimeException {
    public OrderTableRemoveFailException() {
        super("주문테이블을 제거하지 못했습니다.");
    }
}
