package kitchenpos.exception;

public class NotFoundOrderTableException extends KitchenPosException {

    private static final String NOT_FOUND_ORDER_TABLE = "주문테이블을 찾을 수 없습니다.";

    public NotFoundOrderTableException() {
        super(NOT_FOUND_ORDER_TABLE);
    }
}
