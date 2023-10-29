package kitchenpos.domain.tablegroup;

import kitchenpos.domain.tablegroup.TableGroup;

public class UnGroupEvent {
    private final TableGroup tableGroup;

    public UnGroupEvent(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
    }
}
