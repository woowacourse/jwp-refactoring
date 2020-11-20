package kitchenpos.exception;

public class OrderStatusCannotChangeException extends RuntimeException {
    public OrderStatusCannotChangeException() {
        super("완료상태인 주문을 변경할 수 없습니다");
    }
}
