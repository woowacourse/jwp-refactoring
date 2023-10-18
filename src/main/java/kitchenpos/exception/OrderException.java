package kitchenpos.exception;

public class OrderException extends BaseException {

    private final BaseExceptionType exceptionType;

    public OrderException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
