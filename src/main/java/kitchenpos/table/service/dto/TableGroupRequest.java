package kitchenpos.table.service.dto;

import java.util.List;

public class TableGroupRequest {

    private List<TableIdRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final List<TableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequest> getOrderTables() {
        return orderTables;
    }
}
