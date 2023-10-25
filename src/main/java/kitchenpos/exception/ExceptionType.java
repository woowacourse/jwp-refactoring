package kitchenpos.exception;

public enum ExceptionType {

    // product
    PRICE_RANGE(400, "값이 없거나 0보다 작은 가격입니다."),
    PRODUCT_NOT_FOUND(404, "존재하지 않는 상품입니다."),

    // menu group,
    MENU_GROUP_NOT_FOUND(404, "존재하지 않는 메뉴 그룹입니다."),

    // menu
    MENU_PRICE_OVER_SUM(400, "메뉴의 가격이 상품의 가격 합보다 큽니다."),
    MENU_NOT_FOUND(404, "존재하지 않는 메뉴입니다."),

    // order table
    ORDER_TABLE_NOT_FOUND(404, "존재하지 않는 주문 테이블입니다."),
    EMPTY_ORDER_TABLE(400, "주문 테이블이 비어있습니다."),

    // order line item
    EMPTY_ORDER_LINE_ITEMS(400, "주문 항목이 비어있습니다."),
    DUPLICATED_ORDER_LINE_ITEM(400, "주문 항목에 중복된 메뉴가 있습니다."),

    // order
    ALREADY_COMPLETION_ORDER(400, "이미 완료된 주문입니다."),
    ;

    private final int status;
    private final String message;

    ExceptionType(final int status, final String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }
}
