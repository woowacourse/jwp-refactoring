package kitchenpos.application.ordertable;

public class OrderTableChangeStatusRequest {

    private boolean empty;

    private OrderTableChangeStatusRequest() {
    }

    OrderTableChangeStatusRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
