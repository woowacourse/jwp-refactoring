package kitchenpos.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<GroupedTableCreateRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<GroupedTableCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<GroupedTableCreateRequest> getOrderTables() {
        return orderTables;
    }
}
