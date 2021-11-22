package kitchenpos.application.dto.request.table;

import java.util.List;
import kitchenpos.application.dto.request.table.OrderTableIdRequest;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTableIdRequests;

    public TableGroupRequest() {
    }

    public TableGroupRequest(
            List<OrderTableIdRequest> orderTableIdRequests) {
        this.orderTableIdRequests = orderTableIdRequests;
    }

    public List<OrderTableIdRequest> getOrderTableIdRequests() {
        return orderTableIdRequests;
    }
}
