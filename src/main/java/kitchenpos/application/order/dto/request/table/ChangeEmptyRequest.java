package kitchenpos.application.order.dto.request.table;

public class ChangeEmptyRequest {

    private boolean empty;

    public ChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
