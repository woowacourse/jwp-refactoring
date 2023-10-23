package kitchenpos.application.dto.table;

public class TableEmptyChangeRequest {

    private boolean empty;

    private TableEmptyChangeRequest() {
    }

    public TableEmptyChangeRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
