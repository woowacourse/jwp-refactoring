package kitchenpos.common.event;

public class TablesUngroupedEvent {

    private final Long tableGroupId;

    public TablesUngroupedEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
