package kitchenpos.exception;

public class InvalidPriceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String message;

    public InvalidPriceException(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
