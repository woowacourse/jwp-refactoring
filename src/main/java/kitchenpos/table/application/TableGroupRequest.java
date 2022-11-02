package kitchenpos.table.application;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableOfGroupRequest> orderTables;

    private TableGroupRequest() {
    }

    TableGroupRequest(List<OrderTableOfGroupRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableOfGroupRequest> getOrderTables() {
        return orderTables;
    }
}
