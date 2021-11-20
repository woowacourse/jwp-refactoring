package kitchenpos.ui.dto.request;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    public OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
