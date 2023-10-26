package kitchenpos.domain.table_group;

import kitchenpos.support.AggregateReference;

public class TableUnGroupEvent {

    private final AggregateReference<TableGroup> tableGroupId;

    public TableUnGroupEvent(final AggregateReference<TableGroup> tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public AggregateReference<TableGroup> getTableGroupId() {
        return tableGroupId;
    }
}
