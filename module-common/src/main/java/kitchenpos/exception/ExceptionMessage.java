package kitchenpos.exception;

public class ExceptionMessage {

    private final String message;

    public ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
