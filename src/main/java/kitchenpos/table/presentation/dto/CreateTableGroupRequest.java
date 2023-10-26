package kitchenpos.table.presentation.dto;

import java.util.List;

public class CreateTableGroupRequest {

    private List<OrderTableRequest> orderTables;

    private CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
