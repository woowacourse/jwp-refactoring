package kitchenpos.dto.request;

public class TableUpdateEmptyRequest {

    private boolean empty;

    private TableUpdateEmptyRequest() {
    }

    public TableUpdateEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
