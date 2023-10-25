package kitchenpos.table.dto.request;

public class OrderTableUpdateEmptyRequest {

    private boolean empty;

    protected OrderTableUpdateEmptyRequest() {
    }

    public OrderTableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
