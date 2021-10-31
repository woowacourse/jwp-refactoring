package kitchenpos.tablegroup.application;

import java.util.Collections;
import java.util.List;
import kitchenpos.ordertable.domain.OrderTable;

public class TableGroupStartedToCreateEvent {
    private List<OrderTable> orderTables;
    private List<Long> orderTableIds;

    public TableGroupStartedToCreateEvent(List<OrderTable> orderTables, List<Long> orderTableIds) {
        this.orderTables = orderTables;
        this.orderTableIds = orderTableIds;
    }

    public List<OrderTable> getOrderTables() {
        return Collections.unmodifiableList(orderTables);
    }

    public List<Long> getOrderTableIds() {
        return Collections.unmodifiableList(orderTableIds);
    }
}
