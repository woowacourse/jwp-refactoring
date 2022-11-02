package kitchenpos.application.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableGroupCreateRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableGroupCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableGroupCreateRequest> getOrderTables() {
        return orderTables;
    }
}
