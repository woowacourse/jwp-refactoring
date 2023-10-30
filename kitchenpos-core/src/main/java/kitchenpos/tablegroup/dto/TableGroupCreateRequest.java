package kitchenpos.tablegroup.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<Long> orderTables;
    
    public TableGroupCreateRequest(final List<Long> orderTables) {
        this.orderTables = orderTables;
    }
    
    public List<Long> getOrderTables() {
        return orderTables;
    }
}
