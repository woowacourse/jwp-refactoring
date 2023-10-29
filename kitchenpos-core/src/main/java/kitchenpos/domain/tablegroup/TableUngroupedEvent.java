package kitchenpos.domain.tablegroup;

public class TableUngroupedEvent {

    private final Long tableGroupId;

    public TableUngroupedEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
