package kitchenpos.table.ui;

import java.util.List;

public class TableGroupRequest {

    private final List<Long> orderTableIds;

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
