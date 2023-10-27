package kitchenpos.table_group.application.dto;

import java.util.List;
import kitchenpos.order.application.dto.OrderTableIdRequest;

public class TableGroupCreateRequest {

    private List<OrderTableIdRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
