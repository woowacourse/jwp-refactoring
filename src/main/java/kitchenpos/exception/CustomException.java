package kitchenpos.exception;

public class CustomException extends RuntimeException {

    private final int errorCode;

    public CustomException(ExceptionType exceptionType) {
        super(exceptionType.getMessage());
        this.errorCode = exceptionType.getStatus();
    }

    public CustomException(ExceptionType exceptionType, String input) {
        super(exceptionType.getMessage() + " : " + input);
        this.errorCode = exceptionType.getStatus();
    }

    public int getErrorCode() {
        return errorCode;
    }
}
