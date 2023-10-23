package kitchenpos.dto.table;

public class TableUpdateEmptyRequest {

    private final boolean empty;

    public TableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
