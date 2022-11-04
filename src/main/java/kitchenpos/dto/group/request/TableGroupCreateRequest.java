package kitchenpos.dto.group.request;

import java.util.List;
import kitchenpos.dto.order.request.OrderTableRequest;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
