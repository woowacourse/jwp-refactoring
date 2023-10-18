package kitchenpos.exception;

public class InvalidNameException extends RuntimeException {

    private final String message;

    public InvalidNameException(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
