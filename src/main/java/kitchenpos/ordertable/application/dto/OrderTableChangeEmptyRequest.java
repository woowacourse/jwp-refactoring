package kitchenpos.ordertable.application.dto;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    private OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
