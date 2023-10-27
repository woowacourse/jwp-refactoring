package kitchenpos.dto;

public class ChangeEmptyRequest {
    private boolean empty;

    public ChangeEmptyRequest() {
    }

    public ChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
