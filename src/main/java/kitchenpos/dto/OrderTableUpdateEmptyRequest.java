package kitchenpos.dto;

public class OrderTableUpdateEmptyRequest {

    private boolean empty;

    public OrderTableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public OrderTableUpdateEmptyRequest() {
    }

    public boolean getEmpty() {
        return empty;
    }
}
