package kitchenpos.tablegroup.application.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
