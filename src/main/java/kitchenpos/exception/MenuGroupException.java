package kitchenpos.exception;

public class MenuGroupException extends BaseException {

    private final BaseExceptionType exceptionType;

    public MenuGroupException(MenuGroupExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
