package kitchenpos.tablegroup.dto.request;

import java.util.List;
import kitchenpos.ordertable.dto.request.OrderTableRequest;

public class TableGroupRequest {
    private List<OrderTableRequest> orderTableRequests;

    public TableGroupRequest(List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }
}
