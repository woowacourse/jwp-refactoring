package kitchenpos.request;

import java.util.List;

public class TableGroupRequest {

    private final List<TableIdRequest> orderTables;

    public TableGroupRequest(List<TableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequest> getOrderTables() {
        return orderTables;
    }
}
