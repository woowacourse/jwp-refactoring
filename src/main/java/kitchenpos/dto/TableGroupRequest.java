package kitchenpos.dto;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableRequest> orderTableRequests;

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }

    public void setOrderTableRequests(final List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }
}
