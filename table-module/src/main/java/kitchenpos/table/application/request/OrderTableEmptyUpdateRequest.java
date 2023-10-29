package kitchenpos.table.application.request;

public class OrderTableEmptyUpdateRequest {

    private boolean empty;

    public OrderTableEmptyUpdateRequest() {
    }

    public OrderTableEmptyUpdateRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
