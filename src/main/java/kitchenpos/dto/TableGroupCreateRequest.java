package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<TableGroupOrderTableCreateRequest> orderTables;

    public TableGroupCreateRequest(List<TableGroupOrderTableCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableGroupOrderTableCreateRequest> getOrderTables() {
        return orderTables;
    }
}
