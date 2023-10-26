package kitchenpos.domain;

public class TableGroupGroupedEvent {

    private final TableGroup tableGroup;

    public TableGroupGroupedEvent(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
