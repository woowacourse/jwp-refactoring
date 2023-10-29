package kitchenpos.event;

public class TableGroupDeleteEvent {

    private final Long tableGroupId;

    public TableGroupDeleteEvent(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
