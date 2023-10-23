package kitchenpos.application.dto;

public class ErrorResponse {

    private final String message;

    public ErrorResponse(final String message) {
        this.message = message;
    }

    public static ErrorResponse from(final Exception exception) {
        return new ErrorResponse(exception.getMessage());
    }

    public String getMessage() {
        return message;
    }
}
