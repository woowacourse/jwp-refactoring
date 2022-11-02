package kitchenpos.event;

import kitchenpos.table.domain.OrderTable;

public class VerifiedAbleToChangeEmptyEvent {

    private OrderTable orderTable;

    public VerifiedAbleToChangeEmptyEvent(OrderTable orderTable) {
        this.orderTable = orderTable;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }
}
