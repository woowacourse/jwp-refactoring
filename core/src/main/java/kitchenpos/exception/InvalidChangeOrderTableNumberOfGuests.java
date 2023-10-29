package kitchenpos.exception;

public class InvalidChangeOrderTableNumberOfGuests extends IllegalArgumentException {

    public InvalidChangeOrderTableNumberOfGuests(final String message) {
        super(message);
    }
}
