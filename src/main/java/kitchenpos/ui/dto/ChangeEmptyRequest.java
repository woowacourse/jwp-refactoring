package kitchenpos.ui.dto;

public class ChangeEmptyRequest {
    private boolean empty;

    public ChangeEmptyRequest() {
    }

    public ChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
