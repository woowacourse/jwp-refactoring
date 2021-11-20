package kitchenpos.order.dto.request;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableRequest> orderTableRequests;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }

    public void setOrderTableRequests(final List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }
}
