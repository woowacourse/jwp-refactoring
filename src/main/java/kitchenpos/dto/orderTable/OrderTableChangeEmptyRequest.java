package kitchenpos.dto.orderTable;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    private OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
