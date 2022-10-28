package kitchenpos.ui.dto;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private final List<OrderTableRequest> orderTableRequests;


    public TableGroupCreateRequest(final List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }

    public List<Long> getOrderTableIds() {
        return orderTableRequests.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}
