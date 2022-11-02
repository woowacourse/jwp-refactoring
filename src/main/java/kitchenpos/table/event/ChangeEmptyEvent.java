package kitchenpos.table.event;

import kitchenpos.table.domain.OrderTable;

public class ChangeEmptyEvent {

    private final OrderTable orderTable;

    public ChangeEmptyEvent(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
