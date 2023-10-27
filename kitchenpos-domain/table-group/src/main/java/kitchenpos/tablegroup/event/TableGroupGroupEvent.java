package kitchenpos.tablegroup.event;

import java.util.List;

public class TableGroupGroupEvent {

    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public TableGroupGroupEvent(Long tableGroupId, List<Long> orderTableIds) {
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
