package kitchenpos.common;

import kitchenpos.table.dto.OrderTableIdRequest;

import java.util.List;

public class GroupOrderTablesEvent {

    private final List<Long> orderTableIds;
    private final Long tableGroupId;

    public GroupOrderTablesEvent(final List<Long> orderTableIds, final Long tableGroupId) {
        this.orderTableIds = orderTableIds;
        this.tableGroupId = tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
