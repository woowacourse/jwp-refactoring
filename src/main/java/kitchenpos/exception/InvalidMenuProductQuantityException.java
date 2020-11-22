package kitchenpos.exception;

public class InvalidMenuProductQuantityException extends RuntimeException {
    public InvalidMenuProductQuantityException(String message) {
        super(message);
    }
}
