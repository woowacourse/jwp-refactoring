package kitchenpos.ui.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> orderTableIds;

    public TableGroupCreateRequest(final List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public TableGroupCreateRequest() {
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}


