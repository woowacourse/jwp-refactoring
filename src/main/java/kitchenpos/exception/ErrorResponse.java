package kitchenpos.exception;

public class ErrorResponse {

    private final Exception exception;
    private final String message;

    public ErrorResponse(Exception exception, String message) {
        this.exception = exception;
        this.message = message;
    }
}
