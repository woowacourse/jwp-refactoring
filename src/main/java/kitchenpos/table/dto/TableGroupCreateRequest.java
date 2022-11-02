package kitchenpos.table.dto;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public class TableGroupCreateRequest {
    private List<OrderTable> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
