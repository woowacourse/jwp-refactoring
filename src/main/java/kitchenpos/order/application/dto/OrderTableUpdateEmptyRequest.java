package kitchenpos.order.application.dto;

public class OrderTableUpdateEmptyRequest {

    private boolean empty;

    public OrderTableUpdateEmptyRequest() {
    }

    public OrderTableUpdateEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }
}
