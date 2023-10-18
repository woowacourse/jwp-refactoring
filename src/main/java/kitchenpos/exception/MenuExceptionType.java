package kitchenpos.exception;

public enum MenuExceptionType implements BaseExceptionType {

    NULL_PRICE_EXCEPTION("가격은 null일 수 없습니다."),
    NEGATIVE_PRICE_EXCEPTION("가격은 음수일 수 없습니다."),
    PRICE_IS_UNDER_TOTAL_PRODUCT_AMOUNT("메뉴의 가격은 메뉴 상품의 총합보다 낮을 수 없습니다.");

    private final String message;

    MenuExceptionType(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
