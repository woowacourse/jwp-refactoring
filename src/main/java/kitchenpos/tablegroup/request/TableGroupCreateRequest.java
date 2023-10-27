package kitchenpos.tablegroup.request;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<TableGroupUnitDto> orderTables;

    public TableGroupCreateRequest(List<TableGroupUnitDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableGroupUnitDto> getOrderTables() {
        return orderTables;
    }
}
