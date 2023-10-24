package kitchenpos.dto.request;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
