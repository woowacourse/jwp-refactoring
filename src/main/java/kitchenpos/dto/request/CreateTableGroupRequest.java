package kitchenpos.dto.request;

import java.util.List;

public class CreateTableGroupRequest {

    private List<OrderTableRequest> orderTables;

    public CreateTableGroupRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
