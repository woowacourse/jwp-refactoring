package kitchenpos.menu.domain.exception;

import kitchenpos.basic.BasicException;

public class MenuException extends BasicException {

    public MenuException(final MenuExceptionType exceptionType) {
        super(exceptionType);
    }
}
