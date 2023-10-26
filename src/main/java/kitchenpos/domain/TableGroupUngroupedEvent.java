package kitchenpos.domain;

public class TableGroupUngroupedEvent {

    private final TableGroup tableGroup;

    public TableGroupUngroupedEvent(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
