package kitchenpos.ui.dto.request.table;

public class OrderTableEmptyRequest {

    private boolean empty;

    private OrderTableEmptyRequest() {
    }

    public OrderTableEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
