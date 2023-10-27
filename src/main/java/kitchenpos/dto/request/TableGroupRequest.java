package kitchenpos.dto.request;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTableRequests;

    public TableGroupRequest(List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }
}
