package kitchenpos.exception;

public class OrderEmptyException extends KitchenPosException {

    private static final String ORDER_EMPTY = "주문 항목은 비어있을 수 없습니다.";

    public OrderEmptyException() {
        super(ORDER_EMPTY);
    }
}
