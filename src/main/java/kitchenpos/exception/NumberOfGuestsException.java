package kitchenpos.exception;

public class NumberOfGuestsException extends BaseException {

    private final NumberOfGuestsExceptionType exceptionType;

    public NumberOfGuestsException(NumberOfGuestsExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
