package kitchenpos.exception;

public class NotCompletedOrderTableException extends RuntimeException {
    public NotCompletedOrderTableException() {
        super("주문 테이블이 완료 상태가 아닙니다.");
    }
}
