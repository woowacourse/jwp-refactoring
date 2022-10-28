package kitchenpos.dto.request.tableGroup;

import java.util.List;

public class CreateTableGroupRequest {

    private List<AddOrderTableToTableGroupRequest> orderTables;

    public CreateTableGroupRequest(List<AddOrderTableToTableGroupRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<AddOrderTableToTableGroupRequest> getOrderTables() {
        return orderTables;
    }
}
