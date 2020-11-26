package kitchenpos.dto;

public class ExceptionResponse {

    private String errorMessage;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
