package kitchenpos.ui.dto;

public class TableUpdateEmptyRequest {

    private final boolean empty;

    public TableUpdateEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
