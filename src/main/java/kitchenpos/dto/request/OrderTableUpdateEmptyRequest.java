package kitchenpos.dto.request;

public class OrderTableUpdateEmptyRequest {

    private boolean empty;

    public OrderTableUpdateEmptyRequest() {
    }

    public OrderTableUpdateEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
