package kitchenpos.exception;

public enum QuantityExceptionType implements BaseExceptionType {

    VALUE_NEGATIVE_EXCEPTION("수량은 음수가 될 수 없습니다."),
    ;

    private final String message;

    QuantityExceptionType(String message) {
        this.message = message;
    }


    @Override
    public String message() {
        return message;
    }
}
