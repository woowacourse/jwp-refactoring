package kitchenpos.common.exception;

public class InvalidTableException extends BadRequestException {

    public InvalidTableException(String message) {
        super(message);
    }
}
