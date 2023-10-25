package kitchenpos.exception;

public enum ExceptionType {

    // product
    PRICE_RANGE(400, "값이 없거나 0보다 작은 가격입니다."),
    PRODUCT_NOT_FOUND(404, "존재하지 않는 상품입니다."),

    // menu group,
    MENU_GROUP_NOT_FOUND(404, "존재하지 않는 메뉴 그룹입니다."),

    // menu
    MENU_PRICE_OVER_SUM(400, "메뉴의 가격이 상품의 가격 합보다 큽니다."),
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
