package kitchenpos.menu.application.request;

import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.table.application.request.OrderTableRequest;
import kitchenpos.table.domain.OrderTable;

public class TableGroupRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public List<OrderTable> toOrderTables() {
        return orderTables.stream()
            .map(OrderTableRequest::toEntity)
            .collect(Collectors.toUnmodifiableList());
    }
}
