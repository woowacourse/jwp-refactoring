package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private final List<OrderTableRequest> orderTables;

    public TableGroupCreateRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
