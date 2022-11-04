package kitchenpos.tablegroup.dto.request;

public class OrderTableUpdateEmptyRequest {

    private boolean empty;

    private OrderTableUpdateEmptyRequest() {
    }

    public OrderTableUpdateEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
