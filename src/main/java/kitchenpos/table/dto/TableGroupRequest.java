package kitchenpos.table.dto;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public class TableGroupRequest {

    private List<OrderTable> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
