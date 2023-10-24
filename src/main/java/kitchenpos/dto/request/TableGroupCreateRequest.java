package kitchenpos.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableIdRequest> orderTableIds;

    public TableGroupCreateRequest(List<OrderTableIdRequest> orderTableIds) {
        this.orderTableIds = orderTableIds;
    }

    public List<OrderTableIdRequest> orderTableIds() {
        return orderTableIds;
    }
}
