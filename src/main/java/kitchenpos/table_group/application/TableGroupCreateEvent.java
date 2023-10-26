package kitchenpos.table_group.application;

import java.util.List;

public class TableGroupCreateEvent {

    private final Long tableGroupId;
    private final List<Long> tableIds;

    public TableGroupCreateEvent(final Long tableGroupId, final List<Long> tableIds) {
        this.tableGroupId = tableGroupId;
        this.tableIds = tableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }
}
