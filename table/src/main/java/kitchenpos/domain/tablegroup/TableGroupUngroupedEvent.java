package kitchenpos.domain.tablegroup;

public class TableGroupUngroupedEvent {

    private final Long tableGroupId;

    public TableGroupUngroupedEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
