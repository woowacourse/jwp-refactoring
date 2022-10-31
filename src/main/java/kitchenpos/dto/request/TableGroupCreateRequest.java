package kitchenpos.dto.request;

import java.util.List;

public class TableGroupCreateRequest {

    private List<Long> orderTables;

    private TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<Long> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTables() {
        return orderTables;
    }
}
