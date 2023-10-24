package kitchenpos.exception;

public enum ProductExceptionType implements BaseExceptionType {

    NOT_EXIST_PRODUCT_EXCEPTION("상품이 존재하지 않습니다."),
    PRICE_IS_NULL_OR_MINUS_EXCEPTION("상품의 가격은 null이거나 음수일 수 없습니다."),
    ;

    private final String message;

    ProductExceptionType(String message) {
        this.message = message;
    }


    @Override
    public String message() {
        return message;
    }
}
