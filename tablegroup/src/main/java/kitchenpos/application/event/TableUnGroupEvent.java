package kitchenpos.application.event;

public class TableUnGroupEvent {
    private final Long tableGroupId;

    public TableUnGroupEvent(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
