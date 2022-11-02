package kitchenpos.exception;

public class CompletedOrderStatusChangeException extends RuntimeException {
    public CompletedOrderStatusChangeException() {
        super("완료된 주문은 상태를 바꿀 수 없습니다.");
    }
}
