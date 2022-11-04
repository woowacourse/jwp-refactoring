package kitchenpos.dto.common.response;

public class ErrorResponse {

    private String message;

    protected ErrorResponse() {
    }

    public ErrorResponse(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
