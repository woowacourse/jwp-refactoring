package kitchenpos.dto.request.table;

public class ChangeOrderTableEmptyRequest {

    private boolean empty;

    private ChangeOrderTableEmptyRequest() {
    }

    public ChangeOrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

}
