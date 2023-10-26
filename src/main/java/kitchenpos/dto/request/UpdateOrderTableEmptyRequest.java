package kitchenpos.dto.request;

public class UpdateOrderTableEmptyRequest {
    private boolean empty;

    public UpdateOrderTableEmptyRequest() {
    }

    public UpdateOrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
