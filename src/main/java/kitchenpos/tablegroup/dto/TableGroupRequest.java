package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupRequest {
    private List<TableRequest> orderTables;

    protected TableGroupRequest() {
    }

    public TableGroupRequest(final List<TableRequest> orderTableIds) {
        this.orderTables = orderTableIds;
    }

    public List<TableRequest> getOrderTables() {
        return orderTables;
    }
}
