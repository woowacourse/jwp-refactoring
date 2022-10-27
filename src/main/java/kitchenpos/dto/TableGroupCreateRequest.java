package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableCreateRequest> orderTables;

    public TableGroupCreateRequest(List<OrderTableCreateRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableCreateRequest> getOrderTables() {
        return orderTables;
    }
}
