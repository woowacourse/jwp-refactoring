package domain;

import support.AggregateReference;

public class DeleteTableGroupEvent implements TableGroupEvent {

    private final TableGroup tableGroup;

    public DeleteTableGroupEvent(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public AggregateReference<TableGroup> getTableGroupId() {
        return new AggregateReference<>(tableGroup.getId());
    }
}
