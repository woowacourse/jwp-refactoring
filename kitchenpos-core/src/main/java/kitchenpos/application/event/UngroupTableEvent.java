package kitchenpos.application.event;

public class UngroupTableEvent {

    private final Long tableGroupId;

    public UngroupTableEvent(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
