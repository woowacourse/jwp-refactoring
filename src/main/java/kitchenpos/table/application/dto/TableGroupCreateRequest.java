package kitchenpos.table.application.dto;

import java.util.List;
import kitchenpos.table.domain.OrderTable;

public class TableGroupCreateRequest {
    private List<OrderTable> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
