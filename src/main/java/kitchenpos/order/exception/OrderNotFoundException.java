package kitchenpos.order.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException() {
        super("일치하는 주문이 없습니다.");
    }
}
