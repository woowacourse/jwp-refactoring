package kitchenpos.product.exception;

public class InvalidProductPriceException extends RuntimeException {
    
    public InvalidProductPriceException(final String message) {
        super(message);
    }
}
