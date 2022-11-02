package kitchenpos.order.application.dto.request.tablegroup;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
