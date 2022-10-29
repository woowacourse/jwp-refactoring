package kitchenpos.application.request.tablegroup;

import java.util.List;

public class TableGroupRequest {

    private List<OrderTableIdRequest> orderTables;

    public TableGroupRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupRequest(final OrderTableIdRequest... orderTables) {
        this(List.of(orderTables));
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
