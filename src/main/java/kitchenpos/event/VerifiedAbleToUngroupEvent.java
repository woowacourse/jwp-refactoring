package kitchenpos.event;

import java.util.List;

import kitchenpos.table.domain.OrderTable;

public class VerifiedAbleToUngroupEvent {

    private final List<OrderTable> orderTables;

    public VerifiedAbleToUngroupEvent(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
