package kitchenpos.dto;

public class TableEmptyChangeRequest {
    private boolean empty;

    private TableEmptyChangeRequest() {
    }

    public TableEmptyChangeRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
