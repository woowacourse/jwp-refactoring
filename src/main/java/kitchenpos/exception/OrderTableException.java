package kitchenpos.exception;

public class OrderTableException extends BaseException {

    private final OrderTableExceptionType exceptionType;

    public OrderTableException(OrderTableExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public OrderTableExceptionType exceptionType() {
        return exceptionType;
    }
}
