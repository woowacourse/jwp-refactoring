package kitchenpos.tablegroup.application.event.dto;

import kitchenpos.tablegroup.TableGroup;

public class TableGroupDeleteRequestEvent {

    private final TableGroup tableGroup;

    public TableGroupDeleteRequestEvent(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
