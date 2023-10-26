package kitchenpos.menu.domain.exception;

import kitchenpos.basic.BasicExceptionType;

public enum MenuExceptionType implements BasicExceptionType {

    PRICE_IS_BIGGER_THAN_MENU_PRODUCT_PRICES_SUM("메뉴의 가격은, 메뉴 상품의 총 가격보다 클 수 없습니다."),
    MENU_PRODUCT_IS_CONTAIN_NOT_SAVED_PRODUCT("요청하신 메뉴 상품 중, 저장되지 않는 상품이 존재합니다."),
    MENU_GROUP_IS_NOT_FOUND("메뉴 요청한 메뉴그룹이 저장되어 있지 않습니다."),
    MENU_IS_NOT_FOUND("요청하신 메뉴를 찾을 수 없습니다.");

    private final String message;

    MenuExceptionType(final String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
