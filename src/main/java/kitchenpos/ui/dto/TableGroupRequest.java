package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupRequest {
    private final List<OrderTableIdDto> orderTables;

    public TableGroupRequest(List<OrderTableIdDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableIdDto> getOrderTables() {
        return orderTables;
    }
}
