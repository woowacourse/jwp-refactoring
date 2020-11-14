package kitchenpos.exception;

public class InvalidProductPriceException extends RuntimeException {
    public InvalidProductPriceException(String message) {
        super(message);
    }
}
