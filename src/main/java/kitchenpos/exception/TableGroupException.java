package kitchenpos.exception;

public class TableGroupException extends BaseException {

    private final BaseExceptionType exceptionType;

    public TableGroupException(BaseExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }

}
