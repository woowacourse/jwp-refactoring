package kitchenpos.tablegroup.application.event.dto;

import kitchenpos.tablegroup.TableGroup;

import java.util.List;

public class TableGroupCreateRequestEvent {

    private final List<Long> orderTableIds;
    private final TableGroup tableGroup;

    public TableGroupCreateRequestEvent(final List<Long> orderTableIds, final TableGroup tableGroup) {
        this.orderTableIds = orderTableIds;
        this.tableGroup = tableGroup;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }
}
