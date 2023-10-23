package kitchenpos.domain.exception;

public class InvalidOrderTableChangeNumberOfGuestsException extends RuntimeException {
    public InvalidOrderTableChangeNumberOfGuestsException(final String message) {
        super(message);
    }
}
