package kitchenpos.application.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    protected TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}
