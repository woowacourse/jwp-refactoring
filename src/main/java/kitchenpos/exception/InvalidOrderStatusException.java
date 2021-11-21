package kitchenpos.exception;

public class InvalidOrderStatusException extends KitchenPosException {

    private static final String INVALID_ORDER_STATUS = "올바르지 않은 주문 상태입니다.";

    public InvalidOrderStatusException() {
        super(INVALID_ORDER_STATUS);
    }
}
