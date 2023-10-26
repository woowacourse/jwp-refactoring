package kitchenpos.product.exception;

public class NotExistProductException extends RuntimeException {
    public NotExistProductException(final String message) {
        super(message);
    }
}
