package kitchenpos.common.exception;

public enum CustomErrorCode {

    PRICE_MIN_VALUE_ERROR("가격은 0원 이상이어야 합니다."),
    QUANTITY_NEGATIVE_ERROR("수량은 0개 이상이어야 합니다."),
    NAME_BLANK_ERROR("이름은 빈칸일 수 없습니다."),

    PRODUCT_NOT_FOUND_ERROR("존재하지 않는 상품입니다."),

    MENU_PRICE_ERROR("메뉴의 가격은 메뉴 상품들의 가격의 합보다 같거나 작아야 합니다."),

    MENU_GROUP_NOT_FOUND_ERROR("존재하지 않는 메뉴 그룹입니다."),

    APPLICATION_ERROR("처리하지 못한 에러입니다.")
    ;

    private final String message;

    CustomErrorCode(final String message) {
        this.message = message;
    }
}
