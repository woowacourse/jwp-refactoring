package kitchenpos.common.exception;

public class InvalidMenuGroupException extends BadRequestException {

    public InvalidMenuGroupException(String message) {
        super(message);
    }
}
