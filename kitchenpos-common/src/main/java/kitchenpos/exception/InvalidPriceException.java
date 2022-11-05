package kitchenpos.exception;

public class InvalidPriceException extends IllegalArgumentException {

    public InvalidPriceException(final String message) {
        super(message);
    }
}
