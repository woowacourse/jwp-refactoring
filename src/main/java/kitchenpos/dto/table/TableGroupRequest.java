package kitchenpos.dto.table;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableDto> orderTables;

    public TableGroupRequest() {
    }

    public TableGroupRequest(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }
}
