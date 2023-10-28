package kitchenpos.table_group.application;

public class TableGroupUngroupEvent {

    private final Long tableGroupId;

    public TableGroupUngroupEvent(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
