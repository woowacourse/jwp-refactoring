package kitchenpos.dto;

import java.util.List;
import kitchenpos.domain.OrderTable;

public class TableGroupRequest {
    private final List<OrderTable> orderTables;

    public TableGroupRequest(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }
}
