package kitchenpos.application.request;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableRequest> orderTableRequests;

    public TableGroupCreateRequest(List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }
}
