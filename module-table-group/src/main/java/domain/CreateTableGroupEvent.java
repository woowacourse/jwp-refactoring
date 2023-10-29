package domain;

import java.util.List;
import support.AggregateReference;

public class CreateTableGroupEvent implements TableGroupEvent {

    private final List<Long> orderTableIds;
    private final TableGroup tableGroup;

    public CreateTableGroupEvent(final List<Long> orderTableIds, final TableGroup tableGroup) {
        this.orderTableIds = orderTableIds;
        this.tableGroup = tableGroup;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public AggregateReference<TableGroup> getTableGroupId() {
        return new AggregateReference<>(tableGroup.getId());
    }
}
