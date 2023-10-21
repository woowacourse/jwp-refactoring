package kitchenpos.dto.table;

public class TableUpdateEmptyRequest {

    private final boolean empty;

    private TableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public static TableUpdateEmptyRequest from(boolean empty) {
        return new TableUpdateEmptyRequest(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}
