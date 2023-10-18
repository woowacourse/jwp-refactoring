package kitchenpos.dto.request;

import java.util.List;
import java.util.stream.Collectors;

public class TableGroupCreationRequest {

    private final List<OrderTableRequest> orderTables;

    public TableGroupCreationRequest(List<OrderTableRequest> orderTables) {
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
