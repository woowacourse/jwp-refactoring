package kitchenpos.exception;

public enum CustomErrorCode {

    PRICE_MIN_VALUE_ERROR("가격은 0원 이상이어야 합니다."),
    QUANTITY_NEGATIVE_ERROR("수량은 0개 이상이어야 합니다."),

    MENU_PRICE_ERROR("메뉴의 가격은 메뉴 상품들의 가격의 합보다 같거나 작아야 합니다.")
    ;

    private final String message;

    CustomErrorCode(final String message) {
        this.message = message;
    }
}
