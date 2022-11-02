package kitchenpos.application.dto.request;

public class OrderTableSetEmptyRequest {
    private boolean empty;

    public OrderTableSetEmptyRequest() {
    }

    public OrderTableSetEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
