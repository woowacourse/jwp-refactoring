package kitchenpos.exception;

public class InvalidOrderTableException extends RuntimeException {
    public InvalidOrderTableException(String message) {
        super(message);
    }
}
