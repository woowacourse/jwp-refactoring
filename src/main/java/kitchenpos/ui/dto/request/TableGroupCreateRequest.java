package kitchenpos.ui.dto.request;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.OrderTable;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupCreateRequest() {
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
