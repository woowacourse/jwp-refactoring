package kitchenpos.exception;

public class MenuGroupException extends BaseException {

    private final MenuGroupExceptionType exceptionType;

    public MenuGroupException(MenuGroupExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public MenuGroupExceptionType exceptionType() {
        return exceptionType;
    }
}
