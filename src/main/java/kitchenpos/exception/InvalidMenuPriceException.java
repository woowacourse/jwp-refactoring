package kitchenpos.exception;

public class InvalidMenuPriceException extends RuntimeException {
    public InvalidMenuPriceException(String message) {
        super(message);
    }
}
