package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableChangeRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<OrderTableChangeRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableChangeRequest> getOrderTables() {
        return orderTables;
    }
}
