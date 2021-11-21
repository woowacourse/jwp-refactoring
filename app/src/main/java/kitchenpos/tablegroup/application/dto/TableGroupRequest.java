package kitchenpos.tablegroup.application.dto;

import java.util.List;
import kitchenpos.table.application.dto.OrderTableRequest;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
