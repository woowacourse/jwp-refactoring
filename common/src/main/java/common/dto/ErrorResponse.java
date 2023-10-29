package common.dto;

public class ErrorResponse {
    private String message;

    private ErrorResponse(final String message) {
        this.message = message;
    }

    public ErrorResponse() {
    }

    public static ErrorResponse of(final String message) {
        return new ErrorResponse(message);
    }

    public String getMessage() {
        return message;
    }
}
