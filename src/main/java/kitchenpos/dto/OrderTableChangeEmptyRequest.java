package kitchenpos.dto;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    protected OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
