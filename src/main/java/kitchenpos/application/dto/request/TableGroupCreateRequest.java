package kitchenpos.application.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<Long> orderTableIds;

    private TableGroupCreateRequest() {
        this(null);
    }

    public TableGroupCreateRequest(List<Long> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds;
    }
}
