package kitchenpos.dto.tablegroup;

import kitchenpos.domain.table.OrderTableIds;

import java.util.List;

public class TableGroupCreateRequest {
    private List<Long> orderTableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public OrderTableIds toOrderTableIds() {
        return OrderTableIds.from(this.orderTableIds);
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
