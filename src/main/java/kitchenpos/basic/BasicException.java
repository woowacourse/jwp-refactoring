package kitchenpos.basic;

public abstract class BasicException extends RuntimeException{

    private final BasicExceptionType exceptionType;

    public BasicException(final BasicExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.exceptionType = exceptionType;
    }

    public BasicExceptionType getExceptionType() {
        return exceptionType;
    }
}
