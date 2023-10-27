package kitchenpos.tablegroup.event;

public class UngroupOrderTablesEvent {

    private final Long tableGroupId;

    public UngroupOrderTablesEvent(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
