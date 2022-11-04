package kitchenpos.table.dto.request;

import java.util.List;

public class TableGroupRequest {

    private List<TableForGroupingRequest> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<TableForGroupingRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableForGroupingRequest> getOrderTables() {
        return orderTables;
    }
}
