package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.OrderTable;

public class TableGroupRequest {
    private List<OrderTable> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
