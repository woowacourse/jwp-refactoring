package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTablesIds() {
        return orderTables.stream()
            .map(OrderTableRequest::getId)
            .collect(Collectors.toList());
    }
}
