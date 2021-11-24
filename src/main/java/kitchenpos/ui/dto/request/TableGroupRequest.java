package kitchenpos.ui.dto.request;

import java.util.List;

public class TableGroupRequest {

    private List<TableIdRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<TableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequest> getOrderTables() {
        return orderTables;
    }
}
