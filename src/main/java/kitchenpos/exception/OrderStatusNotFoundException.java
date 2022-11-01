package kitchenpos.exception;

public class OrderStatusNotFoundException extends IllegalArgumentException {

    private static final String MESSAGE = "존재하지 않는 주문 상태입니다.";

    public OrderStatusNotFoundException() {
        super(MESSAGE);
    }
}
