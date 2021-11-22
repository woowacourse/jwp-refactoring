package kitchenpos.exception;

public class InvalidOrderTableSizeException extends KitchenPosException {

    private static final String INVALID_ORDER_TABLE_SIZE = "단체 지정시 주문 테이블은 두 개 이상이여야합니다.";

    public InvalidOrderTableSizeException() {
        super(INVALID_ORDER_TABLE_SIZE);
    }
}
