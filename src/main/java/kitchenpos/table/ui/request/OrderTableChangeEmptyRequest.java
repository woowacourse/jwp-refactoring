package kitchenpos.table.ui.request;

public class OrderTableChangeEmptyRequest {

    private boolean empty;

    public static OrderTableChangeEmptyRequest create(boolean empty) {
        final OrderTableChangeEmptyRequest orderTableChangeEmptyRequest = new OrderTableChangeEmptyRequest();
        orderTableChangeEmptyRequest.empty = empty;
        return orderTableChangeEmptyRequest;
    }

    public boolean isEmpty() {
        return empty;
    }
}
