package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableGroupRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableGroupRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableGroupRequest> getOrderTables() {
        return orderTables;
    }
}
