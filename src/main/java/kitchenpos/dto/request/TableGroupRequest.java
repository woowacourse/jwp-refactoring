package kitchenpos.dto.request;

import java.util.List;

public class TableGroupRequest {

    private List<Long> orderTableIds;

    private TableGroupRequest() {
    }

    public TableGroupRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
