package kitchenpos.dto;

public class ChangeOrderTableEmptyRequest {

    private boolean empty;

    public ChangeOrderTableEmptyRequest() {
    }

    public ChangeOrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
