package kitchenpos.exception;

public class OrderTableEmptyGroupIdException extends KitchenPosException {

    private static final String ORDER_TABLE_EMPTY_GROUP_ID = "빈 테이블이 아니거나 그룹 테이블 ID가 null이 아닙니다.";
    public OrderTableEmptyGroupIdException() {
        super(ORDER_TABLE_EMPTY_GROUP_ID);
    }
}
