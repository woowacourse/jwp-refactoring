package kitchenpos.exception;

public class MenuException extends RuntimeException {

    private final MenuExceptionType exceptionType;

    public MenuException(MenuExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public MenuExceptionType exceptionType() {
        return exceptionType;
    }
}
