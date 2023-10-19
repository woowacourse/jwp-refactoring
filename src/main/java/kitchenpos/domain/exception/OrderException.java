package kitchenpos.domain.exception;

import kitchenpos.basic.BasicException;

public class OrderException extends BasicException {

    public OrderException(final OrderExceptionType exceptionType) {
        super(exceptionType);
    }
}
