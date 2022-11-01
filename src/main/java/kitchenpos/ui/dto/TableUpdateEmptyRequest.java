package kitchenpos.ui.dto;

public class TableUpdateEmptyRequest {

    private boolean empty;

    public TableUpdateEmptyRequest() {
    }

    public TableUpdateEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
