package kitchenpos.table.application.dto;

public class OrderTableChangeStatusRequest {

    private boolean empty;

    public OrderTableChangeStatusRequest() {
    }

    public OrderTableChangeStatusRequest(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
