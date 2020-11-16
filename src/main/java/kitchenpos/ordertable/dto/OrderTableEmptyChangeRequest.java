package kitchenpos.ordertable.dto;

public class OrderTableEmptyChangeRequest {
    private boolean empty;

    public OrderTableEmptyChangeRequest() {
    }

    public OrderTableEmptyChangeRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
