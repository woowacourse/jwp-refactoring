package kitchenpos.ordertable.application.dto;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    OrderTableChangeEmptyRequest() {

    }

    public OrderTableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
