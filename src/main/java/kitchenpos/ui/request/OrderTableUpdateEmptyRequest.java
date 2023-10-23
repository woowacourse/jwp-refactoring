package kitchenpos.ui.request;

public class OrderTableUpdateEmptyRequest {

    private boolean empty;
    public OrderTableUpdateEmptyRequest() {
    }

    public OrderTableUpdateEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean getEmpty() {
        return empty;
    }

}
