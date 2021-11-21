package kitchenpos.exception;

public enum ErrorType {
    PRODUCT_PRICE_ERROR("product-001", "상품 가격이 없거나 음수입니다."),
    PRODUCT_NOT_FOUND("product-002", "조회할 수 없는 상품입니다."),

    MENU_PRODUCT_PRICE_ERROR("menu-001", "메뉴의 가격은 상품의 총합과 같거나 작아야합니다."),
    MENU_PRODUCT_EMPTY("menu-002", "메뉴에 포함될 상품은 빈 값일 수 없습니다."),
    MENU_PRICE_ERROR("menu-003", "메뉴의 가격이 없거나 음수입니다."),
    MENU_NOT_FOUND("menu-004", "조회할 수 없는 메뉴입니다."),

    ORDER_NOT_FOUND("order-001", "조회할 수 없는 주문입니다."),
    ORDER_NOT_COMPLETED("order-002", "주문이 완료되지 않았습니다."),
    ORDER_LINE_ITEM_EMPTY("order-003", "주문 아이템은 빈 값일 수 없습니다."),
    ORDER_LINE_ITEM_DUPLICATED("order-004", "주문 아이템이 중복됩니다."),
    ORDER_NOT_COOKING("order-005", "주문이 Cooking 상태가 아닙니다."),
    ORDER_ALREADY_COMPLETED("order-006", "주문이 이미 Completion 상태입니다."),

    TABLE_NOT_FOUND("table-001", "조회할 수 없는 테이블입니다."),
    TABLE_EMPTY_ERROR("table-002", "비어있는 테이블입니다."),
    TABLE_NOT_EMPTY_ERROR("table-003", "비어있지 않은 테이블입니다."),
    TABLE_GROUP_ERROR("table-004", "테이블 그룹을 만드려면 최소 2개 이상의 테이블이 필요합니다."),
    TABLE_GROUP_DUPLICATED("table-005", "테이블 그룹의 테이블이 중복됩니다."),
    TABLE_NUMBER_OF_GUEST_ERROR("table-006", "테이블의 손님은 1명 이상이여야 합니다."),
    TABLE_GROUP_NOT_NULL("table-007", "테이블 그룹에 포함되어 있습니다.");

    ErrorType(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    private final String errorCode;
    private final String message;

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
