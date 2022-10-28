package kitchenpos.application.event;

import kitchenpos.domain.TableGroup;

public class TableGroupEvent {
    private final TableGroup tableGroup;

    public TableGroupEvent(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
