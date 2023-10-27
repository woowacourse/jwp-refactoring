package kitchenpos.domain.exception;

public class InvalidOrderTableToTableGroup extends IllegalArgumentException {

    public InvalidOrderTableToTableGroup(final String message) {
        super(message);
    }
}
