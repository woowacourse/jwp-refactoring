package kitchenpos.application.dto;

public class TableEmptyRequest {

    private boolean empty;

    public TableEmptyRequest() {
    }

    public TableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
