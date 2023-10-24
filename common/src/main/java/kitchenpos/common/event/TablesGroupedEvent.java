package kitchenpos.common.event;

import java.util.List;

public class TablesGroupedEvent {

    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public TablesGroupedEvent(Long tableGroupId, List<Long> orderTableIds) {
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
