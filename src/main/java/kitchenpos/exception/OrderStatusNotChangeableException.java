package kitchenpos.exception;

public class OrderStatusNotChangeableException extends RuntimeException {
    public OrderStatusNotChangeableException() {
        super("계산이 완료된 주문은 상태를 바꿀 수 없습니다!");
    }
}
