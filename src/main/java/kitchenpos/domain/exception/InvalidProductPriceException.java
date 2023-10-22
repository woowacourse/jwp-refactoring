package kitchenpos.domain.exception;

public class InvalidProductPriceException extends RuntimeException {
    
    public InvalidProductPriceException(final String message) {
        super(message);
    }
}
