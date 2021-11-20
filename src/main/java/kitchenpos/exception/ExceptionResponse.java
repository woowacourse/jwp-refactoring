package kitchenpos.exception;

public class ExceptionResponse {

    private String message;

    protected ExceptionResponse() {
    }

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
