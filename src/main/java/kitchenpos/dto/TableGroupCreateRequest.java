package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<OrderTableId> orderTables;

    public TableGroupCreateRequest() {
    }

    public List<OrderTableId> getOrderTables() {
        return orderTables;
    }
}
