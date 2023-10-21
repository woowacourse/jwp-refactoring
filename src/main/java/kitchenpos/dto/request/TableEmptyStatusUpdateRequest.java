package kitchenpos.dto.request;

public class TableEmptyStatusUpdateRequest {

    private boolean empty;

    public TableEmptyStatusUpdateRequest() {
    }

    public TableEmptyStatusUpdateRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
