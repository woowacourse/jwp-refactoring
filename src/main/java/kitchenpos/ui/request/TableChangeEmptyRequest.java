package kitchenpos.ui.request;

public class TableChangeEmptyRequest {

    private boolean empty;

    public TableChangeEmptyRequest() {
    }

    public TableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
