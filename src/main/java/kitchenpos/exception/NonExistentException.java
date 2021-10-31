package kitchenpos.exception;

public class NonExistentException extends BadRequestException {
    public NonExistentException(String message) {
        super(message);
    }
}
