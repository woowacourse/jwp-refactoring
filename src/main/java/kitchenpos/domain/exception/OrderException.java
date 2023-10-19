package kitchenpos.domain.exception;

import kitchenpos.basic.BasicException;
import kitchenpos.basic.BasicExceptionType;

public class OrderException extends BasicException {

    public OrderException(final BasicExceptionType exceptionType) {
        super(exceptionType);
    }
}
