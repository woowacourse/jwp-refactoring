package kitchenpos.exception;

public class InappropriateOrderTableException extends RuntimeException {
    public InappropriateOrderTableException(String message) {
        super(message);
    }
}
