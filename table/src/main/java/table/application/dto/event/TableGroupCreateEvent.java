package table.application.dto.event;

import java.util.List;
import table.domain.OrderTable;

public class TableGroupCreateEvent {

    private final List<OrderTable> orderTables;

    public TableGroupCreateEvent(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
