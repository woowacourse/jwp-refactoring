package kitchenpos.dto.tablegroup.request;

import java.util.List;

public class TableGroupCreateRequest {
    private final List<Long> orderTableIds;

    public TableGroupCreateRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> orderTableIds() {
        return orderTableIds;
    }
}
