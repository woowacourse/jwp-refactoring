package kitchenpos.table.dto;

public class OrderTableChangeEmptyRequest {
    private final boolean empty;
    public OrderTableChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
