package kitchenpos.exception;

public enum NumberOfGuestsExceptionType implements BaseExceptionType {

    NEGATIVE_VALUE_EXCEPTION("손님 수는 음수가 될 수 없습니다."),
    ;

    private final String message;

    NumberOfGuestsExceptionType(String message) {
        this.message = message;
    }

    @Override
    public String message() {
        return message;
    }
}
