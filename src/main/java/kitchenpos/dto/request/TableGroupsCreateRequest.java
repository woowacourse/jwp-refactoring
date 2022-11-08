package kitchenpos.dto.request;

import java.util.List;

public class TableGroupsCreateRequest {
    private List<OrderTableIdRequest> orderTables;

    public TableGroupsCreateRequest() {
    }

    public TableGroupsCreateRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
