package kitchenpos.dto.request;

import java.util.List;

public class CreateTableGroupRequest {
    private List<TableIdRequest> orderTables;

    public CreateTableGroupRequest() {
    }

    public CreateTableGroupRequest(List<TableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<TableIdRequest> getOrderTables() {
        return orderTables;
    }
}
