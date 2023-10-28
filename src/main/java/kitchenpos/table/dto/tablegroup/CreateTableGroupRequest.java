package kitchenpos.table.dto.tablegroup;

import java.util.List;

public class CreateTableGroupRequest {

    private final List<OrderTableIdRequest> orderTables;

    public CreateTableGroupRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
