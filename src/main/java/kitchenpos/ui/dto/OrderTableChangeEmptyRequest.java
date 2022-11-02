package kitchenpos.ui.dto;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    public OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
