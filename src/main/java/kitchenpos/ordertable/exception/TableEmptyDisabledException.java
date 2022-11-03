package kitchenpos.ordertable.exception;

public class TableEmptyDisabledException extends IllegalArgumentException {

    public TableEmptyDisabledException(String message) {
        super(message);
    }
}
