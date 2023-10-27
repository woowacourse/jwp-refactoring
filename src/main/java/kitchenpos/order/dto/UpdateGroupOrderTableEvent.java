package kitchenpos.order.dto;

import java.util.List;

public class UpdateGroupOrderTableEvent {

    private final Long tableGroupId;
    private final List<Long> orderTableIds;

    public UpdateGroupOrderTableEvent(final Long tableGroupId, final List<Long> orderTableIds) {
        this.tableGroupId = tableGroupId;
        this.orderTableIds = orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
