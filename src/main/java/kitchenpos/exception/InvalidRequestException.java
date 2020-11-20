package kitchenpos.exception;

public class InvalidRequestException extends RuntimeException {

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException() {
        super("부적절한 요청입니다");
    }
}
