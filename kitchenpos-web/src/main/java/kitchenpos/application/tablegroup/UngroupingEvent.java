package kitchenpos.application.tablegroup;

public class UngroupingEvent {
    private final Long tableGroupId;

    public UngroupingEvent(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
