package kitchenpos.ui.dto;

public class TableChangeEmptyRequest {

    private boolean empty;

    private TableChangeEmptyRequest() {
    }

    public TableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
