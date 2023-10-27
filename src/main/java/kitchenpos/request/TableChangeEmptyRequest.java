package kitchenpos.request;

public class TableChangeEmptyRequest {

    private final boolean empty;

    public TableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
