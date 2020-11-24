package kitchenpos.common;

public class CreateFailException extends RuntimeException {
    public CreateFailException() {
    }

    public CreateFailException(String message) {
        super(message);
    }
}
