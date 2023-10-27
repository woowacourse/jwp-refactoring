package kitchenpos.domain.table_group;

import java.util.List;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.support.AggregateReference;

public class CreateTableGroupEvent implements TableGroupEvent {

    private final List<AggregateReference<OrderTable>> orderTableIds;
    private final TableGroup tableGroup;

    public CreateTableGroupEvent(
            final List<AggregateReference<OrderTable>> orderTableIds,
            final TableGroup tableGroup
    ) {
        this.orderTableIds = orderTableIds;
        this.tableGroup = tableGroup;
    }

    public List<AggregateReference<OrderTable>> getOrderTableIds() {
        return orderTableIds;
    }

    public AggregateReference<TableGroup> getTableGroupId() {
        return new AggregateReference<>(tableGroup.getId());
    }
}
