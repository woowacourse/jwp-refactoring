package kitchenpos.order.presentation.dto;

public class ChangeEmptyRequest {

    private boolean empty;

    private ChangeEmptyRequest() {
    }

    public ChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
