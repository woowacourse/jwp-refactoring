package kitchenpos.dto.ordertable;

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
