package kitchenpos.table.ui.dto.request;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    private OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
