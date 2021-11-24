package kitchenpos.menu.dto;

public class OrderTableEmptyRequest {

    private boolean empty;

    protected OrderTableEmptyRequest() {
    }

    public OrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
