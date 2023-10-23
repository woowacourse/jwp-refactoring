package kitchenpos.exception;

public enum ExceptionInformation {
    // 100 번대 도메인 관련 오류
    // 1__: 상품
    PRODUCT_NAME_IS_NULL(100, "상품 이름은 비어있을 수 없습니다"),
    PRODUCT_NAME_LENGTH_OUT_OF_BOUNCE(101, "상품 이름 글자수가 허용 범위 밖입니다"),
    PRODUCT_PRICE_IS_NULL(102, "상품 가격은 비어있을 수 없습니다"),
    PRODUCT_PRICE_LENGTH_OUT_OF_BOUNCE(103, "상품 가격이 허용 범위 밖입니다"),
    PRODUCT_NOT_FOUND(104, "존재하지 않는 상품입니다"),

    // 2__: 메뉴 그룹
    MENU_GROUP_NAME_IS_NULL(200, "메뉴 그룹 이름은 비어있을 수 없습니다"),
    MENU_GROUP_NAME_LENGTH_OUT_OF_BOUNCE(201, "메뉴 그룹 이름 글자수가 허용 범위 밖입니다"),
    MENU_GROUP_NOT_FOUND(202, "존재하지 않는 메뉴 분류입니다"),

    // 3__: 메뉴
    MENU_NAME_IS_NULL(300, "메뉴 이름은 비어있을 수 없습니다"),
    MENU_NAME_LENGTH_OUT_OF_BOUNCE(301, "메뉴 이름 글자수가 허용 범위 밖입니다"),
    MENU_PRICE_IS_NULL(302, "메뉴 가격은 비어있을 수 없습니다"),
    MENU_PRICE_LENGTH_OUT_OF_BOUNCE(303, "메뉴 가격이 허용 범위 밖입니다"),
    MENU_QUANTITY_OUT_OF_BOUNCE(304, "메뉴에 속하는 상품의 개수가 허용 범위 밖입니다"),
    MENU_PRICE_OVER_MENU_PRODUCT_PRICE(305, "메뉴의 가격이 메뉴에 속하는 상품의 가격 총합보다 큽니다"),

    // 4__: 주문
    ORDER_LINE_ITEMS_IS_EMPTY(400,"주문 항목이 비었습니다"),
    ORDER_ITEM_NOT_FOUND_OR_DUPLICATE(401,"주문 항목에 중복되거나 존재하지 않는 메뉴가 존재합니다"),
    UPDATE_COMPLETED_ORDER(403, "완료된 주문의 상태를 변경할 수 없습니다"),
    ORDER_IN_EMPTY_TABLE(404, "empty 상태인 테이블의 주문을 생성할 수 없습니다"),


    // 5__: 주문_테이블
    ORDER_TABLE_NOT_FOUND(500, "해당하는 주문 테이블이 존재하지 않습니다")
    ;

    private int code;

    private String message;

    ExceptionInformation(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
