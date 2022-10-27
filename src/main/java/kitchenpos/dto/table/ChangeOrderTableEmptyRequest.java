package kitchenpos.dto.table;

public class ChangeOrderTableEmptyRequest {

    private boolean empty;

    public ChangeOrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

}
