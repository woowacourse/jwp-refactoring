package kitchenpos.order.dto;

import java.util.List;

public class UpdateGroupOrderTableDto {

    private Long tableGroupId;
    private List<Long> orderTableIds;

    public UpdateGroupOrderTableDto(final Long tableGroupId, final List<Long> orderTableIds) {
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
