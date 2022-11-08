package kitchenpos.table.event;

import java.util.List;

import kitchenpos.table.domain.OrderTable;

public class UngroupedEvent {

    private final List<OrderTable> orderTables;

    public UngroupedEvent(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
