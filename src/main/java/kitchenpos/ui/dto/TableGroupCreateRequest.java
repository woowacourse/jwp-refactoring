package kitchenpos.ui.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableId> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(final List<OrderTableId> orderTables) {
        this.orderTables = orderTables;
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }
}
