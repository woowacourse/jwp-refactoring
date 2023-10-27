package kitchenpos.exception;

public class InvalidMenuPriceException extends IllegalArgumentException {

    public InvalidMenuPriceException(final String message) {
        super(message);
    }
}
