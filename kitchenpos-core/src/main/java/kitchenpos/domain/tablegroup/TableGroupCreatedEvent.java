package kitchenpos.domain.tablegroup;

import java.util.List;

public class TableGroupCreatedEvent {

    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public TableGroupCreatedEvent(Long tableGroupId, List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
