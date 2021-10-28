package kitchenpos.exception;

public class InvalidRequestParamException extends BadRequestException {

    public InvalidRequestParamException(String message) {
        super(message);
    }
}
