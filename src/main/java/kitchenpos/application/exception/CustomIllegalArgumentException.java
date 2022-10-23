package kitchenpos.application.exception;

public class CustomIllegalArgumentException extends IllegalArgumentException{
    public CustomIllegalArgumentException(final ExceptionType type) {
        super(type.getMessage());
    }
}
