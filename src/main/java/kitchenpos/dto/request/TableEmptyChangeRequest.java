package kitchenpos.dto.request;

public class TableEmptyChangeRequest {
    private boolean empty;

    public TableEmptyChangeRequest() {
    }

    public TableEmptyChangeRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
