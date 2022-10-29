package kitchenpos.application.dto;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    public OrderTableChangeEmptyRequest() {
    }

    public OrderTableChangeEmptyRequest(final boolean isEmpty) {
        this.empty = isEmpty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
