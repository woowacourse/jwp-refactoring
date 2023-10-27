package kitchenpos.table.dto;

import java.util.List;

public class TableGroupRequest {

    private final List<Long> orderTableIds;

    public TableGroupRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
