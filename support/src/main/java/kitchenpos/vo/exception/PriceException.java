package kitchenpos.vo.exception;

import kitchenpos.basic.BasicException;
import kitchenpos.basic.BasicExceptionType;

public class PriceException extends BasicException {

    public PriceException(final BasicExceptionType exceptionType) {
        super(exceptionType);
    }
}
