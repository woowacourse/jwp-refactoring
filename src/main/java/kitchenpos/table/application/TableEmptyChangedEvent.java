package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;

public class TableEmptyChangedEvent {

    private final OrderTable orderTable;

    public TableEmptyChangedEvent(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
