package kitchenpos.application.dto;

import java.util.List;

public class TableGroupRequest {

    private List<Long> orderTableIds;

    public TableGroupRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public TableGroupRequest() {
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
