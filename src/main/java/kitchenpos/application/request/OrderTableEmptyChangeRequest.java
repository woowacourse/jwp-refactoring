package kitchenpos.application.request;

public class OrderTableEmptyChangeRequest {

    private boolean empty;

    private OrderTableEmptyChangeRequest() {
    }

    public OrderTableEmptyChangeRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
