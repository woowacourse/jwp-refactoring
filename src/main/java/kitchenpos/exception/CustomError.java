package kitchenpos.exception;

public enum CustomError {

    PRICE_MIN_VALUE_ERROR("가격은 0원 이상이어야 합니다."),
    QUANTITY_NEGATIVE_ERROR("수량은 0개 이상이어야 합니다."),
    NAME_BLANK_ERROR("이름은 빈칸일 수 없습니다."),

    PRODUCT_NOT_FOUND_ERROR("존재하지 않는 상품입니다."),

    MENU_NOT_FOUND_ERROR("존재하지 않는 메뉴입니다."),
    MENU_PRICE_ERROR("메뉴의 가격은 메뉴 상품들의 가격의 합보다 같거나 작아야 합니다."),

    MENU_GROUP_NOT_FOUND_ERROR("존재하지 않는 메뉴 그룹입니다."),

    ORDER_STATUS_ALREADY_COMPLETED_ERROR("계산 완료 후에는 주문 상태를 변경할 수 없습니다."),
    ORDER_STATUS_CHANGE_SAME_ERROR("같은 주문 상태로를 변경할 수 없습니다."),
    ORDER_NOT_FOUND_ERROR("존재하지 않는 주문입니다."),
    ORDER_ITEM_EMPTY_ERROR("주문 항목이 없습니다."),
    ORDER_TABLE_EMPTY_ERROR("주문하려는 테이블은 비어있으면 안됩니다."),
    TABLE_EMPTY_CHANGE_SAME_ERROR("빈 테이블인지 여부는 같은 상태로 변경할 수 없습니다."),
    TABLE_GUEST_NUMBER_NEGATIVE_ERROR("테이블의 방문 손님 수는 0명 이상이어야 합니다."),
    TABLE_STATUS_INVALID_ERROR("테이블의 방문 손님이 1명 이상이면 테이블을 비어있는 상태이면 안된다."),
    TABLE_EMPTY_BUT_CHANGE_GUEST_NUMBER_ERROR("빈테이블에는 방문 손님 수를 변경할 수 없다."),
    UNCOMPLETED_ORDER_IN_TABLE_ERROR("계산이 완료되지 않은 주문이 있습니다."),
    TABLE_GROUP_MIN_TABLES_ERROR("테이블 그룹에는 2개 이상의 테이블이 속해 있어야 합니다."),
    TABLE_GROUP_TABLE_NOT_EMPTY_ERROR("빈 테이블만 단체 지정할 수 있습니다."),
    TABLE_ALREADY_GROUPED_ERROR("이미 그룹에 속해있는 테이블입니다."),

    TABLE_NOT_FOUND_ERROR("존재하지 않는 테이블입니다."),
    TABLE_GROUP_NOT_FOUND_ERROR("존재하지 않는 단체 지정입니다."),

    REQUEST_PROPERTY_INVALID_ERROR("잘못된 요청 정보입니다."),

    APPLICATION_ERROR("처리하지 못한 에러입니다.")
    ;

    private final String message;

    CustomError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
