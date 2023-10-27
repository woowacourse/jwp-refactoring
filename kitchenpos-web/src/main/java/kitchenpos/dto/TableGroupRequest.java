package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest {
    private List<Long> OrderTableIds;

    public TableGroupRequest() {
    }

    public TableGroupRequest(final List<Long> orderTableIds) {
        OrderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return OrderTableIds;
    }
}
