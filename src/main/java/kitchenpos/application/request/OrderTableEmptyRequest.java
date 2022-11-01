package kitchenpos.application.request;

public class OrderTableEmptyRequest {

    private boolean empty;

    public OrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
