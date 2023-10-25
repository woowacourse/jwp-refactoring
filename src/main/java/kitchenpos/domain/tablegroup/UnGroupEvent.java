package kitchenpos.domain.tablegroup;

public class UnGroupEvent {
    private final TableGroup tableGroup;

    public UnGroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
