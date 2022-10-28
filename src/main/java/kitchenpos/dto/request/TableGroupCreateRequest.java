package kitchenpos.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<TableIdRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<TableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequest> getOrderTables() {
        return orderTables;
    }
}
