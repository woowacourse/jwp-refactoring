package kitchenpos.common.exception;

public class InvalidProductException extends BadRequestException {

    public InvalidProductException(String message) {
        super(message);
    }
}
