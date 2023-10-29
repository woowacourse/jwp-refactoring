package table.application.dto.event;

import java.util.List;
import table.domain.OrderTable;

public class TableGroupUnGroupEvent {

    private final List<OrderTable> orderTables;

    public TableGroupUnGroupEvent(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
