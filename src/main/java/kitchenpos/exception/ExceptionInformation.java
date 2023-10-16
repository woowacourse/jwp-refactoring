package kitchenpos.exception;

public enum ExceptionInformation {
    // 100 번대 도메인 관련 오류
    // 1__: 상품
    PRODUCT_NAME_IS_NULL(100, "상품 이름은 비어있을 수 없습니다"),
    PRODUCT_NAME_LENGTH_OUT_OF_BOUNCE(101, "상품 이름 글자수가 허용 범위 밖입니다"),
    PRODUCT_PRICE_LENGTH_OUT_OF_BOUNCE(102, "상품 가격이 허용 범위 밖입니다"),
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
