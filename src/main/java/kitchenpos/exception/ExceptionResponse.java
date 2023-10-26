package kitchenpos.exception;

public class ExceptionResponse {

    private final String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
