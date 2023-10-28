package kitchenpos.table.dto;

public class TableUpdateEmptyRequest {

    private boolean empty;

    private TableUpdateEmptyRequest() {
    }

    public TableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
