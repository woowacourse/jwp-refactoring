package kitchenpos.ui.dto;

public class OrderTableUpdateEmptyRequest {

    private final boolean empty;

    public OrderTableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
