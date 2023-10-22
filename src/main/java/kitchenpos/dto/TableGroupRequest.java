package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest {

    List<Long> orderTableId;

    public TableGroupRequest(final List<Long> orderTableId) {
        this.orderTableId = orderTableId;
    }

    public List<Long> getOrderTableId() {
        return orderTableId;
    }
}
