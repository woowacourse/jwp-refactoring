package kitchenpos.dto.request;

import java.util.List;

public class TableGroupCreateRequest {
    private List<OrderTableId> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }
}
