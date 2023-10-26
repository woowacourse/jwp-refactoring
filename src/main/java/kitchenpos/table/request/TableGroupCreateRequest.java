package kitchenpos.table.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    private List<OrderTableIdRequest> orderTableIds;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableIdRequest> orderTableIdRequests) {
        this.orderTableIds = orderTableIdRequests;
    }

    public List<Long> getOrderTableIds() {
        return orderTableIds.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
