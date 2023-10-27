package kitchenpos.tableGroup.dto;

import kitchenpos.table.dto.OrderTableIdRequest;

import java.util.List;

public class TableGroupRequest {

    private final List<OrderTableIdRequest> orderTables;

    public TableGroupRequest(final List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }
}
