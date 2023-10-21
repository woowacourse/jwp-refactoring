package kitchenpos.domain.exception;

import kitchenpos.basic.BasicException;

public class OrderTableException extends BasicException {

    public OrderTableException(final OrderTableExceptionType exceptionType) {
        super(exceptionType);
    }
}
