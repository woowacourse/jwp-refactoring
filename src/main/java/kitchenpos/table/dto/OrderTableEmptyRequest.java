package kitchenpos.table.dto;

public class OrderTableEmptyRequest {

    private boolean empty;

    private OrderTableEmptyRequest() {
    }

    public OrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
