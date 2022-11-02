package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;

public class EmptyChangedEvent {

    private final OrderTable orderTable;

    public EmptyChangedEvent(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
