package kitchenpos.order.exception;

import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;

public class OrderException extends BaseException {

    private final BaseExceptionType exceptionType;

    public OrderException(OrderExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
