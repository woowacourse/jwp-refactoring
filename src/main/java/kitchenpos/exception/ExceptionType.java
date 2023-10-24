package kitchenpos.exception;

public enum ExceptionType {

    // product
    PRODUCT_PRICE(400, "값이 없거나 0보다 작은 가격입니다."),
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
