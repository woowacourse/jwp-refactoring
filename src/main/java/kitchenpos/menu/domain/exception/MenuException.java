package kitchenpos.menu.domain.exception;

import kitchenpos.basic.BasicException;
import kitchenpos.basic.BasicExceptionType;

public class MenuException extends BasicException {

    public MenuException(final BasicExceptionType exceptionType) {
        super(exceptionType);
    }
}
