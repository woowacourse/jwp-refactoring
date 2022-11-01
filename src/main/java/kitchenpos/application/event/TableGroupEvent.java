package kitchenpos.application.event;

import java.util.List;
import kitchenpos.domain.TableGroup;

public class TableGroupEvent {
    private final TableGroup tableGroup;
    private final List<Long> orderTableIds;

    public TableGroupEvent(final TableGroup tableGroup, final List<Long> orderTableIds) {
        this.tableGroup = tableGroup;
        this.orderTableIds = orderTableIds;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
