package kitchenpos.application.dto;

public class OrderTableEmptyRequest {

    private final boolean empty;

    public OrderTableEmptyRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
