package kitchenpos.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
        super("주문이 저장되어 있지 않습니다.");
    }
}
