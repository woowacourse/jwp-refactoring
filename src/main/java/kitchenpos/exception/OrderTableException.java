package kitchenpos.exception;

public class OrderTableException extends BaseException {

    private final BaseExceptionType exceptionType;

    public OrderTableException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
