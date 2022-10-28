package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableRequest> orderTableRequests;


    public TableGroupCreateRequest(final List<OrderTableRequest> orderTableRequests) {
        this.orderTableRequests = orderTableRequests;
    }

    public List<OrderTableRequest> getOrderTableRequests() {
        return orderTableRequests;
    }
}
