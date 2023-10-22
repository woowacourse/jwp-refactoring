package kitchenpos.vo.tablegroup;

import java.util.List;

public class TableGroupRequest {
    private final List<Long> orderTables;

    public TableGroupRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
