package kitchenpos.exception;

public class FieldNotValidException extends BadRequestException {

    public FieldNotValidException(String message) {
        super(message);
    }
}
