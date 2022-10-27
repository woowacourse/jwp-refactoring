package kitchenpos.application.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableCreateRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableCreateRequest> getOrderTables() {
        return orderTables;
    }
}
