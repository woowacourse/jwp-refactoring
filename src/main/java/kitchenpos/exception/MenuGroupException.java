package kitchenpos.exception;

public class MenuGroupException extends RuntimeException {

    private final MenuGroupExceptionType exceptionType;

    public MenuGroupException(MenuGroupExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    public MenuGroupExceptionType exceptionType() {
        return exceptionType;
    }
}
