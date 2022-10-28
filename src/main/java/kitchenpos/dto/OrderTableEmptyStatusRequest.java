package kitchenpos.dto;

public class OrderTableEmptyStatusRequest {

    private final boolean empty;

    public OrderTableEmptyStatusRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
