package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTableRequests;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableIdRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public List<OrderTableIdRequest> getOrderTableRequests() {
        return orderTableRequests;
    }
}
