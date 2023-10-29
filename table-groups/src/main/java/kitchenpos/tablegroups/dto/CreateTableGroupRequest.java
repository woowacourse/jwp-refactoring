package kitchenpos.tablegroups.dto;

import kitchenpos.table.dto.OrderTableRequest;

import java.util.List;

public class CreateTableGroupRequest {
    private List<OrderTableRequest> orderTables;

    private CreateTableGroupRequest(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public CreateTableGroupRequest() {
    }

    public static CreateTableGroupRequest of(final List<OrderTableRequest> orderTables) {
        return new CreateTableGroupRequest(orderTables);
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
