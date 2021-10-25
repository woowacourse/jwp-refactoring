package kitchenpos.exception;

public class KitchenposException extends IllegalArgumentException{
    public static final String ILLEGAL_PRICE = "옳지 않은 가격입니다.";
    public static final String ILLEGAL_MENU_GROUP_ID = "존재하지 않는 메뉴그룹입니다.";
    public static final String ILLEGAL_PRODUCT_ID = "존재하지 않는 메뉴그룹입니다.";
    public static final String IMPOSSIBLE_MENU_PRICE = "메뉴 가격이 상품 가격의 총합보다 비쌀 수 없습니다.";
    public static final String ILLEGAL_ORDER_TABLE_ID = "존재하지 않는 주문 테이블입니다.";
    public static final String IMPOSSIBLE_TABLE_GROUP_ID = "그룹에 포함되는 테이블은 비울 수 없습니다.";
    public static final String IMPOSSIBLE_TABLE_STATUS = "식사가 완료되지 않은 테이블은 비울 수 없습니다.";
    public static final String IMPOSSIBLE_NUMBER_OF_GUESTS = "손님이 음수일 수 없습니다.";
    public static final String EMPTY_ORDER_TABLE = "주문 테이블이 비어있습니다.";
    public static final String ILLEGAL_TABLE_SIZE_MINIMUM = "테이블 그룹에 테이블은 2대 이상이어야 합니다.";
    public static final String ILLEGAL_TABLE_SIZE = "테이블 그룹 내 테이블 수가 올바르지 않습니다.";
    public static final String NOT_EMPTY_TABLE_TO_CREATE = "테이블이 비어있지 않습니다.";

    public KitchenposException(String message) {
        super(message);
    }
}
