package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
