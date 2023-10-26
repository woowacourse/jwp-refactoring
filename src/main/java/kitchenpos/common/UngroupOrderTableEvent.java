package kitchenpos.common;

public class UngroupOrderTableEvent {

    private final Long tableGroupId;

    public UngroupOrderTableEvent(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
