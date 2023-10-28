package kitchenpos.tablegroup.domain;

import java.util.List;

public class TableGroupingEvent {

    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public TableGroupingEvent(final Long tableGroupId, final List<Long> orderTableIds) {
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
