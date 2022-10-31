package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableRequest> orderTables;

    public TableGroupCreateRequest(List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroupCreateRequest() {
    }

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }
}
