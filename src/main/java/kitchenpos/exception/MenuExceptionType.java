package kitchenpos.exception;

public enum MenuExceptionType implements BaseExceptionType {

    MENU_NOT_FOUND("메뉴를 찾을 수 없습니다"),
    MENU_PRODUCT_COMMANDS_CAN_NOT_NULL("메뉴 상품들은 널일 수 없습니다"),
    SUM_OF_MENU_PRODUCTS_PRICE_MUST_BE_LESS_THAN_PRICE("메뉴 가격은 메뉴 상품 가격들의 합보다 작아야 합니다");

    private final String errorMessage;

    MenuExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
