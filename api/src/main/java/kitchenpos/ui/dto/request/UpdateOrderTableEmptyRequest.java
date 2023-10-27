package kitchenpos.ui.dto.request;

public class UpdateOrderTableEmptyRequest {

    private boolean empty;

    public UpdateOrderTableEmptyRequest() {
    }

    public UpdateOrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
