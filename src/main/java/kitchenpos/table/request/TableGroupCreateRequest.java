package kitchenpos.table.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableDto> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
