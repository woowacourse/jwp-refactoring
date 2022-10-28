package kitchenpos.common.exception;

public class InvalidMenuException extends BadRequestException {

    public InvalidMenuException(String message) {
        super(message);
    }
}
