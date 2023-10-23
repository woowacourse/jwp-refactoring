package kitchenpos.dto;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
