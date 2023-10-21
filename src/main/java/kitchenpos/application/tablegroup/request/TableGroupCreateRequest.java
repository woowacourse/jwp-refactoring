package kitchenpos.application.tablegroup.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {
    private List<OrderTableIdRequest> orderTables;

    protected TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableIdRequest::getId)
                .collect(Collectors.toList());
    }
}
