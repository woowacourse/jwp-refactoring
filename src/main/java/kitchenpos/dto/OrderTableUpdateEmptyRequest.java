package kitchenpos.dto;

public class OrderTableUpdateEmptyRequest {

    private final boolean empty;

    public OrderTableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
