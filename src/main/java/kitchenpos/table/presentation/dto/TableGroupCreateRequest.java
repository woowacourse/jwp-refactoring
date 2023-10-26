package kitchenpos.table.presentation.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<Long> orderTables;

    public TableGroupCreateRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
