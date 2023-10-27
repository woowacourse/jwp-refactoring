package kitchenpos.domain.exception;

public class InvalidOrderTablesSize extends IllegalArgumentException {

    public InvalidOrderTablesSize(final String message) {
        super(message);
    }
}
