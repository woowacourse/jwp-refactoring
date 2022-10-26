package kitchenpos.application.dto.request;

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
