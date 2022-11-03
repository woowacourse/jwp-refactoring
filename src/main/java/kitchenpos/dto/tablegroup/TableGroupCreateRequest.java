package kitchenpos.dto.tablegroup;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dto.orderTable.OrderTableRequest;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableRequest::getId)
                .collect(Collectors.toList());
    }
}
