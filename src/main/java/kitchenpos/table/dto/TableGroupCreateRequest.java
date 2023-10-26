package kitchenpos.table.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<SingleOrderTableCreateRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<SingleOrderTableCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<SingleOrderTableCreateRequest> getOrderTables() {
        return orderTables;
    }
}
