package kitchenpos.tablegroup.dto;

import java.util.List;
import kitchenpos.order.dto.OrderTableRequest;

public class TableGroupRequest {
    private final List<OrderTableRequest> orderTables;

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
