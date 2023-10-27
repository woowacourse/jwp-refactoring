package kitchenpos.common.event;

public class UpdateUngroupOrderTableEvent {

    private final Long tableGroupId;

    public UpdateUngroupOrderTableEvent(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
