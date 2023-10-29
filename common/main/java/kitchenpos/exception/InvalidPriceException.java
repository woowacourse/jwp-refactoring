package kitchenpos.exception;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(final String message) {
        super(message);
    }
}
