package kitchenpos.table.event;

import java.util.List;

import kitchenpos.table.domain.OrderTable;

public class UngroupEvent {

    private final List<OrderTable> orderTables;

    public UngroupEvent(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
