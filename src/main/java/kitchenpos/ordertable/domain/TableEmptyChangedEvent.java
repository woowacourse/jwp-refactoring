package kitchenpos.ordertable.domain;

public class TableEmptyChangedEvent {

    private final OrderTable orderTable;
    private final boolean empty;

    public TableEmptyChangedEvent(OrderTable orderTable, boolean empty) {
        this.orderTable = orderTable;
        this.empty = empty;
    }

    public OrderTable orderTable() {
        return orderTable;
    }

    public boolean empty() {
        return empty;
    }
}
