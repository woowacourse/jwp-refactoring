package kitchenpos.ui.dto.request;

import java.util.List;

public class TableGroupRequest {

    List<Long> orderTableIds;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
