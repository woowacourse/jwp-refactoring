package kitchenpos.application.dto.tablegroup;

import java.util.List;

public class TableGroupRequest {

    private List<TableOfGroupDto> orderTables;

    private TableGroupRequest() {}

    public TableGroupRequest(final List<TableOfGroupDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableOfGroupDto> getOrderTables() {
        return orderTables;
    }
}
