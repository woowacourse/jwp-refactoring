package kitchenpos.order.exception;

public class IllegalOrderStatusException extends RuntimeException {
    public IllegalOrderStatusException(String message) {
        super(message);
    }
}
