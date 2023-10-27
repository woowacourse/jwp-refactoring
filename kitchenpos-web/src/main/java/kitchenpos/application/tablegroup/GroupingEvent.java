package kitchenpos.application.tablegroup;

import java.util.List;

public class GroupingEvent {
    private final List<Long> tableIds;
    private final Long tableGroupId;

    public GroupingEvent(final List<Long> tableIds, final Long tableGroupId) {
        this.tableIds = tableIds;
        this.tableGroupId = tableGroupId;
    }

    public List<Long> getTableIds() {
        return tableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
