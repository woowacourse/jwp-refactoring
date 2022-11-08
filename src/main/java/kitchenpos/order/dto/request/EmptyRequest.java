package kitchenpos.order.dto.request;

public class EmptyRequest {

    private boolean empty;

    private EmptyRequest() {
    }

    public EmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
