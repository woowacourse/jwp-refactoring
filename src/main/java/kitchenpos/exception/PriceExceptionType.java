package kitchenpos.exception;

public enum PriceExceptionType implements BaseExceptionType {

    PRICE_IS_NULL_EXCEPTION("가격은 null일 수 없습니다"),
    PRICE_IS_NEGATIVE_EXCEPTION("가격은 음수일 수 없습니다."),
    ;

    private final String message;

    PriceExceptionType(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
