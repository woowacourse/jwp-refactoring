package kitchenpos.exception.dto;

public class ExceptionResponse {
    private String errorCode;
    private String message;

    public ExceptionResponse(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
