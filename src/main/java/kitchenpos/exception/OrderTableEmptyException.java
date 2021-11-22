package kitchenpos.exception;

public class OrderTableEmptyException extends KitchenPosException {

    private static final String ORDER_TABLE_EMPTY = "주문 테이블이 비어있습니다.";

    public OrderTableEmptyException() {
        super(ORDER_TABLE_EMPTY);
    }
}
