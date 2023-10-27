package kitchenpos.exception.table;

import kitchenpos.exception.BaseException;

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
