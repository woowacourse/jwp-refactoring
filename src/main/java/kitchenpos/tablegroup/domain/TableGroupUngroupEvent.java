package kitchenpos.tablegroup.domain;

import java.util.List;

public class TableGroupUngroupEvent {
    private final List<Long> tableGroupIds;

    public TableGroupUngroupEvent(List<Long> tableGroupIds) {
        this.tableGroupIds = tableGroupIds;
    }

    public List<Long> getTableGroupIds() {
        return tableGroupIds;
    }
}
