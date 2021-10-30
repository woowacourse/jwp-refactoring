package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupRequest {
    private List<OrderTableIdRequest> orderTables;

    public List<OrderTableIdRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(List<OrderTableIdRequest> orderTables) {
        this.orderTables = orderTables;
    }
}
