package kitchenpos.exception;

public class QuantityException extends BaseException {

    private final QuantityExceptionType exceptionType;

    public QuantityException(QuantityExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
