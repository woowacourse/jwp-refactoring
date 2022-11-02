package kitchenpos.table.application.request;

public class ChangeOrderTableEmptyRequest {

    private final Long orderTableId;
    private final boolean empty;

    public ChangeOrderTableEmptyRequest(Long orderTableId, boolean empty) {
        this.orderTableId = orderTableId;
        this.empty = empty;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public boolean isEmpty() {
        return empty;
    }
}
