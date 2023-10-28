package kitchenpos.order.application.dto;

import java.util.List;

public class OrderTableGroupEventDto {

    private List<Long> orderTableIds;
    private Long tableGroupId;

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setOrderTableIds(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }
}
