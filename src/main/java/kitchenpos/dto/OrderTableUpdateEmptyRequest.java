package kitchenpos.dto;

public class OrderTableUpdateEmptyRequest {

    private boolean empty;

    public OrderTableUpdateEmptyRequest() {
    }

    public OrderTableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
