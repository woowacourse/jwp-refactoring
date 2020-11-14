package kitchenpos.exception;

public class InvalidOrderLineItemCreateRequestsException extends RuntimeException {
    public InvalidOrderLineItemCreateRequestsException(String message) {
        super(message);
    }
}
