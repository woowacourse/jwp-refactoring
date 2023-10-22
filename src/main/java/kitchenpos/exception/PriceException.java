package kitchenpos.exception;

public class PriceException extends BaseException {

    private final BaseExceptionType exceptionType;

    public PriceException(PriceExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
