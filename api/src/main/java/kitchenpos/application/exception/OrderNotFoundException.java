package kitchenpos.application.exception;

public class OrderNotFoundException extends IllegalArgumentException {

    public OrderNotFoundException() {
        super("지정한 주문을 찾을 수 없습니다.");
    }
}
