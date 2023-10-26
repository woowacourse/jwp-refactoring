package kitchenpos.order.domain;

public class NotExistOrderTable extends RuntimeException {
    public NotExistOrderTable(final String message) {
        super(message);
    }
}
