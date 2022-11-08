package kitchenpos.table.dto.request;

public class TableChangeEmptyRequest {

    private boolean empty;

    public TableChangeEmptyRequest() {
    }

    public TableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
