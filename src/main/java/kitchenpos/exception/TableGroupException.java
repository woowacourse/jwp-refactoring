package kitchenpos.exception;

public class TableGroupException extends BaseException {

    private final BaseExceptionType exceptionType;

    public TableGroupException(TableGroupExceptionType exceptionType) {
        this.exceptionType = exceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return exceptionType;
    }
}
