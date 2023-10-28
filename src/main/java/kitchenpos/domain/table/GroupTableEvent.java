package kitchenpos.domain.table;

import java.util.List;

public class GroupTableEvent {

    private final TableGroup tableGroup;
    private final List<Long> orderTableIds;

    public GroupTableEvent(final TableGroup tableGroup, final List<Long> orderTableIds) {
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
