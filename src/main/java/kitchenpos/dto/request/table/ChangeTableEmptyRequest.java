package kitchenpos.dto.request.table;

public class ChangeTableEmptyRequest {
    private boolean empty;

    public ChangeTableEmptyRequest() {
    }

    public ChangeTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
