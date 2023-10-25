package kitchenpos.request;

import java.util.List;

public class TableGroupRequest {

    private final List<Long> orderTables;

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getTableIds() {
        return orderTables;
    }
}
