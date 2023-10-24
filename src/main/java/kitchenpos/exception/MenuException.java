package kitchenpos.exception;

public class MenuException extends BaseException {

    private final MenuExceptionType exceptionType;

    public MenuException(MenuExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public MenuExceptionType exceptionType() {
        return exceptionType;
    }
}
