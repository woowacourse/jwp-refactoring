package kitchenpos.domain.table_group;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.support.AggregateReference;

public class TableGroupEvent {

    private final List<AggregateReference<OrderTable>> orderTableIds;
    private final AggregateReference<TableGroup> tableGroupId;

    public TableGroupEvent(
            final List<AggregateReference<OrderTable>> orderTableIds,
            final AggregateReference<TableGroup> tableGroupId
    ) {
        this.orderTableIds = orderTableIds;
        this.tableGroupId = tableGroupId;
    }

    public List<AggregateReference<OrderTable>> getOrderTableIds() {
        return orderTableIds;
    }

    public AggregateReference<TableGroup> getTableGroupId() {
        return tableGroupId;
    }
}
