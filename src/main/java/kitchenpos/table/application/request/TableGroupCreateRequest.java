package kitchenpos.table.application.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableIdRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
