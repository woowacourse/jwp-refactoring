package kitchenpos.dto;

public class TableChangeEmptyRequest {
    private boolean empty;

    private TableChangeEmptyRequest() {
    }

    public TableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
