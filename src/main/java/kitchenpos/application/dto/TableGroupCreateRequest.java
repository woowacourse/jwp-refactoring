package kitchenpos.application.dto;

import java.util.List;

public class TableGroupCreateRequest {
    private List<IdRequest> orderTables;

    public TableGroupCreateRequest() {
    }

    public TableGroupCreateRequest(List<IdRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public List<IdRequest> getOrderTables() {
        return orderTables;
    }
}
