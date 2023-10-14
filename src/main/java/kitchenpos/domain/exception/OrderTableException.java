package kitchenpos.domain.exception;

import kitchenpos.basic.BasicException;
import kitchenpos.basic.BasicExceptionType;

public class OrderTableException extends BasicException {

    public OrderTableException(final BasicExceptionType exceptionType) {
        super(exceptionType);
    }
}
