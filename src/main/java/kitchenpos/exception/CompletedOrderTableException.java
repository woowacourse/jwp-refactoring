package kitchenpos.exception;

public class CompletedOrderTableException extends RuntimeException {
    public CompletedOrderTableException() {
        super("완료 상태인 주문 테이블입니다.");
    }
}
