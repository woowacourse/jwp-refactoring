package kitchenpos.tablegroup.application.dto;

import java.util.List;

public final class TableGroupCreateRequest {

    private final List<Long> orderTableIds;

    public TableGroupCreateRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
