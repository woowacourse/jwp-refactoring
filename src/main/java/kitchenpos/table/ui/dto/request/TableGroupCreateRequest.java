package kitchenpos.table.ui.dto.request;

import kitchenpos.table.domain.OrderTable;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.stream()
                .map(OrderTableRequest::toEntity)
                .collect(toList());
    }
}
