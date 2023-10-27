package kitchenpos.exception.menu;

import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;

public class MenuException extends BaseException {

    private final MenuExceptionType exceptionType;

    public MenuException(MenuExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
