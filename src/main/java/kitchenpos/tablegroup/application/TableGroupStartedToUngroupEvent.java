package kitchenpos.tablegroup.application;

public class TableGroupStartedToUngroupEvent {
    private Long tableGroupId;

    public TableGroupStartedToUngroupEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
