package kitchenpos.domain.exception;

public class InvalidMenuPriceException extends RuntimeException {
    public InvalidMenuPriceException(final String message) {
        super(message);
    }
}
