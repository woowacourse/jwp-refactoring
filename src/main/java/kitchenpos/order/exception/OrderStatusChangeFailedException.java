package kitchenpos.order.exception;

public class OrderStatusChangeFailedException extends RuntimeException {

    public OrderStatusChangeFailedException() {
        super("주문 상태 변경에 실패하였습니다.");
    }
}
