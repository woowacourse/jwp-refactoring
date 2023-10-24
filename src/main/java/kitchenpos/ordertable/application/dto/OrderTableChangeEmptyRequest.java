package kitchenpos.ordertable.application.dto;

public class OrderTableChangeEmptyRequest {

    private final boolean empty;

    public OrderTableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
