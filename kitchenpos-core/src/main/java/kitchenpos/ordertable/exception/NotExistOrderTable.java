package kitchenpos.ordertable.exception;

public class NotExistOrderTable extends RuntimeException {
    public NotExistOrderTable(final String message) {
        super(message);
    }
}
