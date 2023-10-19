package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> orderTableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}