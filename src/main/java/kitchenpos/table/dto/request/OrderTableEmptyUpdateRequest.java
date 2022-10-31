package kitchenpos.table.dto.request;

public class OrderTableEmptyUpdateRequest {

    private boolean empty;

    private OrderTableEmptyUpdateRequest() {
    }

    public OrderTableEmptyUpdateRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
